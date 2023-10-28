package com.jochmen.account.repository;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class AccountDatabaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column
    private String email;
    @Column
    private String password;

    public AccountDatabaseEntity() {

    }

    public AccountDatabaseEntity(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountDatabaseEntity that = (AccountDatabaseEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(email, that.email) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password);
    }
}
