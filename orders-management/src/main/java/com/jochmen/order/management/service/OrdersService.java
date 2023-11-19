package com.jochmen.order.management.service;

import com.jochmen.order.management.controller.schema.request.OrderRequest;
import com.jochmen.order.management.repository.OrderDatabaseEntity;
import com.jochmen.order.management.repository.OrdersRepository;
import com.jochmen.order.management.service.message.OrderMessage;
import com.jochmen.order.management.service.response.ProductResponse;
import io.micrometer.observation.annotation.Observed;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Observed(name = "ordersService")
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
                        .anyMatch(productResponse -> orderRequest.productIds().stream()
                                .anyMatch(productId -> productResponse.id().equals(productId) && !productResponse.accountID().equals(accountId))))
                .onErrorReturn(false)
                .block());

        if (canProductBeOrdered) {
            var orders = orderRequest.productIds().stream()
                    .map(productId -> new OrderDatabaseEntity(productId, accountId))
                    .toList();
            ordersRepository.saveAll(orders);

            rabbitTemplate.convertAndSend(ORDERS_QUEUE_NAME,
                    new OrderMessage(orders.stream()
                        .map(OrderDatabaseEntity::getProductId)
                        .toList(), accountId)
            );
        }
        return canProductBeOrdered;
    }
}
