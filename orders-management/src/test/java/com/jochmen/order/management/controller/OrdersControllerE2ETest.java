package com.jochmen.order.management.controller;

import com.jochmen.order.management.TestConfig;
import com.jochmen.order.management.controller.schema.request.OrderRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.jochmen.order.management.TestConfig.*;
import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TestConfig.class})
@Testcontainers
@ActiveProfiles("test")
class OrdersControllerE2ETest {

    @Container
    private static final RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.9-management")
            .withExposedPorts(5672, 15672)
            .withEnv("RABBITMQ_DEFAULT_USER", "guest")
            .withEnv("RABBITMQ_DEFAULT_PASS", "guest")
            .withQueue("/", "ordersQueue");

    @Autowired
    private TestRestTemplate restTemplate;

    @DynamicPropertySource
    static void configureRabbitProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
    }

    @Test
    void testCreateOrder_Success() {
        OrderRequest orderRequest = new OrderRequest(List.of(PRODUCT_ID));
        HttpHeaders headers = new HttpHeaders();
        headers.set("account-header", ACCOUNT_ID_2.toString());
        HttpEntity<OrderRequest> requestEntity = new HttpEntity<>(orderRequest, headers);

        ResponseEntity<Void> response = restTemplate.postForEntity("/orders", requestEntity, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testCreateOrder_Failure() {
        OrderRequest invalidRequest = new OrderRequest(List.of(PRODUCT_ID));
        HttpHeaders headers = new HttpHeaders();
        headers.set("account-header", ACCOUNT_ID.toString());
        HttpEntity<OrderRequest> requestEntity = new HttpEntity<>(invalidRequest, headers);

        ResponseEntity<Void> response = restTemplate.postForEntity("/orders", requestEntity, Void.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}