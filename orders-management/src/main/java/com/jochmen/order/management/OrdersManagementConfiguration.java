package com.jochmen.order.management;

import io.micrometer.core.instrument.observation.DefaultMeterObservationHandler;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Profile("!test")
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

    @Bean
    public ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
        var meterRegistry = new SimpleMeterRegistry();
        observationRegistry.observationConfig().observationHandler(new DefaultMeterObservationHandler(meterRegistry));
        return new ObservedAspect(observationRegistry);
    }
}
