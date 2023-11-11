package com.jochmen.order.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OrdersManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrdersManagementApplication.class);
    }
}
