package com.jochmen.order.management;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@TestConfiguration
@Profile("test")
public class TestConfig {

    private WireMockServer wireMockServer;

    public static final UUID ACCOUNT_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
    public static final UUID ACCOUNT_ID_2 = UUID.fromString("123e4567-e89b-12d3-a456-426614174002");
    public static final UUID PRODUCT_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    @PostConstruct
    public void setupWireMock() {
        wireMockServer = new WireMockServer(8082);
        wireMockServer.start();

        configureFor("localhost", 8082);

        stubFor(get(urlPathMatching("/products.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"id\":\"" + PRODUCT_ID + "\",\"name\":\"Test Product\",\"accountID\":\"" + ACCOUNT_ID + "\"}]")));
    }

    @PreDestroy
    public void teardownWireMock() {
        wireMockServer.stop();
    }

    @Bean
    @Primary
    public WebClient.Builder productsWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8082");
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