package com.jochmen.order.consumer.service.message;

import java.util.List;
import java.util.UUID;

public record OrderMessage(List<UUID> productIds, UUID accountId) {
}
