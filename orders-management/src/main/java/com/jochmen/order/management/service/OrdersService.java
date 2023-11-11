package com.jochmen.order.management.service;

import com.jochmen.order.management.controller.schema.request.OrderRequest;
import com.jochmen.order.management.repository.OrderDatabaseEntity;
import com.jochmen.order.management.repository.OrdersRepository;
import com.jochmen.order.management.service.message.OrderMessage;
import com.jochmen.order.management.service.response.ProductResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
public class OrdersService {

    private static final String ORDERS_QUEUE_NAME = "ordersQueue";
    private final WebClient.Builder productsWebClient;
    private final RabbitTemplate rabbitTemplate;
    private final OrdersRepository ordersRepository;

    public OrdersService(WebClient.Builder productsWebClient,
                         RabbitTemplate rabbitTemplate,
                         OrdersRepository ordersRepository) {
        this.productsWebClient = productsWebClient;
        this.rabbitTemplate = rabbitTemplate;
        this.ordersRepository = ordersRepository;
    }

    public boolean createOrder(OrderRequest orderRequest, UUID accountId) {
        var canProductBeOrdered =  Boolean.TRUE.equals(productsWebClient.build().get()
                .uri(uriBuilder -> uriBuilder
                        .pathSegment("products")
                        .build())
                        .header("account-header", accountId.toString())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ProductResponse>>(){})
                .map(products -> products.stream()
                        .anyMatch(productResponse -> productResponse.id().equals(orderRequest.productId()) && !productResponse.accountID().equals(accountId)))
                .onErrorReturn(false)
                .block());

        if (canProductBeOrdered) {
            ordersRepository.save(new OrderDatabaseEntity(orderRequest.productId(), accountId));
            rabbitTemplate.convertAndSend(ORDERS_QUEUE_NAME, new OrderMessage(orderRequest.productId(), accountId));
        }
        return canProductBeOrdered;
    }
}
