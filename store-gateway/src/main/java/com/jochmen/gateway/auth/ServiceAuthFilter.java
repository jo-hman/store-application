package com.jochmen.gateway.auth;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class ServiceAuthFilter extends AbstractGatewayFilterFactory<ServiceAuthFilter.AuthConfiguration> {

    public ServiceAuthFilter() {
        super(AuthConfiguration.class);
    }

    @Override
    public GatewayFilter apply(AuthConfiguration config) {
        return ((exchange, chain) -> {
            //TODO
            return chain.filter(exchange);
        });
    }

    public static class AuthConfiguration {

    }
}
