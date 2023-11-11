package com.jochmen.gateway;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class StoreGatewayConfiguration {

    private static final String ACCOUNTS_BASE_URL = "lb://ACCOUNT/";

    @Bean
    @LoadBalanced
    WebClient.Builder accountsWebClient() {
        return WebClient.builder()
                .baseUrl(ACCOUNTS_BASE_URL);
    }
}
