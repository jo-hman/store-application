package com.jochmen.order.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrdersRepository extends JpaRepository<OrderDatabaseEntity, UUID> {
}
