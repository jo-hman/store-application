package org.jochmen.product.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "products")
public class ProductDatabaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column
    private String name;

    @Column
    private UUID accountId;

    public ProductDatabaseEntity() {

    }

    public ProductDatabaseEntity(String name, UUID accountId) {
        this.name = name;
        this.accountId = accountId;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UUID getAccountId() {
        return accountId;
    }
}
