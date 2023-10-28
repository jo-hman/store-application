package com.jochmen.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountsRepository extends JpaRepository<AccountDatabaseEntity, UUID> {
    boolean existsByEmail(String email);

    Optional<AccountDatabaseEntity> findByEmailAndPassword(String email, String password);
}
