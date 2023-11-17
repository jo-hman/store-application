package com.jochmen.order.consumer.service;

import com.jochmen.order.consumer.service.message.OrderMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class OrdersConsumerService {

    @RabbitListener(queues = "ordersQueue")
    public void listen(OrderMessage orderMessage) {
        //todo REST call to product module to assign product
        System.out.println("Message arrived from ordersQueue: " + orderMessage.productIds() + " " + orderMessage.accountId());
    }

}
