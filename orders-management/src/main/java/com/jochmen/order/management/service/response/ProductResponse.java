package com.jochmen.order.management.service.response;

import java.util.UUID;

public record ProductResponse(UUID id, String name, UUID accountID) {
}
