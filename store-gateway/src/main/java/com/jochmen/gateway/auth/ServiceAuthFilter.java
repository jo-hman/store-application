package com.jochmen.gateway.auth;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ServiceAuthFilter extends AbstractGatewayFilterFactory<ServiceAuthFilter.AuthConfiguration> {

    @Qualifier("accountsWebClient")
    private final WebClient.Builder accountsWebClient;

    public ServiceAuthFilter(WebClient.Builder accountsWebClient) {
        super(AuthConfiguration.class);
        this.accountsWebClient = accountsWebClient;
    }

    @Override
    public GatewayFilter apply(AuthConfiguration config) {
        return ((exchange, chain) -> {
            var headers = exchange.getRequest().getHeaders();
            if (headers.containsKey(HttpHeaders.AUTHORIZATION)) {
                return accountsWebClient.build().get()
                        .uri(uriBuilder -> uriBuilder
                                .pathSegment("accounts", "access-codes", "validate")
                                .queryParam("code", getAccessCode(headers))
                                .build())
                        .retrieve()
                        .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new UnauthorizedAccessException()))
                        .toBodilessEntity()
                        .flatMap(value -> chain.filter(exchange))
                        .onErrorResume(UnauthorizedAccessException.class, error -> getUnauthorizedResponse(exchange));
            }
            return getUnauthorizedResponse(exchange);
        });
    }

    private static Mono<Void> getUnauthorizedResponse(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(401));
        return exchange.getResponse().setComplete();
    }

    private static String getAccessCode(HttpHeaders headers) {
        return headers.get(HttpHeaders.AUTHORIZATION).get(0).substring(7);
    }

    public static class AuthConfiguration {

    }

}
