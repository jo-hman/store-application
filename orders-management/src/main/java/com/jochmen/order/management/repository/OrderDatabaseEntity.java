package com.jochmen.order.management.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class OrderDatabaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column
    private UUID productId;

    @Column
    private UUID accountId;

    public OrderDatabaseEntity() {
    }

    public OrderDatabaseEntity(UUID productId, UUID accountId) {
        this.productId = productId;
        this.accountId = accountId;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public UUID getId() {
        return id;
    }

    public UUID getProductId() {
        return productId;
    }

    public UUID getAccountId() {
        return accountId;
    }
}
