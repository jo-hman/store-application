package com.jochmen.order.management.controller;

import com.jochmen.order.management.controller.schema.request.OrderRequest;
import com.jochmen.order.management.service.OrdersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    private final static String ACCOUNT_HEADER = "account-header";

    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody OrderRequest orderRequest, @RequestHeader(ACCOUNT_HEADER) String accountId) {
        return ordersService.createOrder(orderRequest, UUID.fromString(accountId)) ?
                ResponseEntity.ok().build() :
                ResponseEntity.badRequest().build();
    }

}
