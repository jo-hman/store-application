package com.jochmen.order.consumer.service;

import com.jochmen.order.consumer.service.message.OrderMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class OrdersConsumerService {

    @RabbitListener(queues = "ordersQueue")
    public void listen(OrderMessage orderMessage) {
        System.out.println("Message arrived from ordersQueue: " + orderMessage.productId() + " " + orderMessage.accountId());
    }

}
