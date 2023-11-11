package com.jochmen.order.consumer.service.message;

import java.util.UUID;

public record OrderMessage(UUID productId, UUID accountId) {
}
