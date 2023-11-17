package com.jochmen.order.management;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OrdersManagementConfiguration {

    private static final String PRODUCTS_BASE_URL = "lb://PRODUCT/";

    @Bean
    @LoadBalanced
    WebClient.Builder productsWebClient() {
        return WebClient.builder()
                .baseUrl(PRODUCTS_BASE_URL);
    }

    @Bean
    public Queue ordersQueue() {
        return new Queue("ordersQueue", true);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
