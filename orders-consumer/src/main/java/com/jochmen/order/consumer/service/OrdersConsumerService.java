package com.jochmen.order.consumer.service;

import com.jochmen.order.consumer.service.message.OrderMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class OrdersConsumerService {

    private static final Logger log = LoggerFactory.getLogger(OrdersConsumerService.class);

    @RabbitListener(queues = "ordersQueue")
    public void listen(OrderMessage orderMessage) {
        log.info("New message arrived! Order has been placed for account {} and products {}",
                orderMessage.accountId(), orderMessage.productIds());
    }

}
