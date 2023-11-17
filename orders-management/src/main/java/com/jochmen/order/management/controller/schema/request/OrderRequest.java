package com.jochmen.order.management.controller.schema.request;

import java.util.List;
import java.util.UUID;

public record OrderRequest(List<UUID> productIds) {
}
