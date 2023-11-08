package org.jochmen.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductsRepository extends JpaRepository<ProductDatabaseEntity, UUID> {

    Optional<ProductDatabaseEntity> findByName(String name);

    List<ProductDatabaseEntity> findAllByAccountId(UUID accountId);

    Optional<ProductDatabaseEntity> findByIdAndAccountId(UUID id, UUID accountId);
}
