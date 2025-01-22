package com.jochmen.order.management.service;

import com.jochmen.order.management.controller.schema.request.OrderRequest;
import com.jochmen.order.management.repository.OrderDatabaseEntity;
import com.jochmen.order.management.repository.OrdersRepository;
import com.jochmen.order.management.service.message.OrderMessage;
import com.jochmen.order.management.service.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrdersServiceTest {

    @Mock
    private WebClient.Builder webClientBuilder;
    @Mock
    private WebClient webClient;
    @Mock
    private RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private RequestHeadersSpec requestHeadersSpec;
    @Mock
    private ResponseSpec responseSpec;
    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private OrdersRepository ordersRepository;

    @InjectMocks
    private OrdersService ordersService;

    private UUID accountId;
    private OrderRequest orderRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountId = UUID.randomUUID();
        orderRequest = new OrderRequest(List.of(UUID.randomUUID()));

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(any(), any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void testCreateOrder_Success() {
        ProductResponse productResponse = new ProductResponse(orderRequest.productIds().get(0), "Test", UUID.randomUUID());

        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.just(List.of(productResponse)));


        boolean result = ordersService.createOrder(orderRequest, accountId);

        assertTrue(result);
        verify(ordersRepository, times(1)).saveAll(any());
        verify(rabbitTemplate, times(1)).convertAndSend(eq("ordersQueue"), any(OrderMessage.class));
    }

    @Test
    void testCreateOrder_ProductNotOrderable() {
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.just(List.of()));

        boolean result = ordersService.createOrder(orderRequest, accountId);

        assertFalse(result);
        verify(ordersRepository, never()).saveAll(any());
        verify(rabbitTemplate, never()).convertAndSend(anyString(), Optional.ofNullable(any()));
    }

    @Test
    void testCreateOrder_ErrorInProductService() {
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.error(new RuntimeException("Service down")));

        boolean result = ordersService.createOrder(orderRequest, accountId);

        assertFalse(result);
        verify(ordersRepository, never()).saveAll(any());
        verify(rabbitTemplate, never()).convertAndSend(anyString(), Optional.ofNullable(any()));
    }
}
