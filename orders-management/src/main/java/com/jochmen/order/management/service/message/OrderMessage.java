package com.jochmen.order.management.service.message;

import java.util.UUID;

public record OrderMessage(UUID productId, UUID accountId) {
}
