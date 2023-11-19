package org.jochmen.product.service;

import io.micrometer.observation.annotation.Observed;
import org.jochmen.product.controller.schema.request.ProductCreationRequest;
import org.jochmen.product.repository.ProductDatabaseEntity;
import org.jochmen.product.repository.ProductsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Observed(name = "productsService")
@Service
public class ProductsService {

    private final ProductsRepository productsRepository;

    public ProductsService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }


    public Optional<UUID> createProduct(ProductCreationRequest productCreationRequest, UUID accountId) {
        return productsRepository.findByName(productCreationRequest.name())
                .map(productDatabaseEntity -> Optional.<UUID>empty())
                .orElseGet(() -> create(productCreationRequest, accountId));
    }

    private Optional<UUID> create(ProductCreationRequest productCreationRequest, UUID accountId) {
        return Optional.of(productsRepository
                .save(new ProductDatabaseEntity(productCreationRequest.name(), accountId)).getId());
    }

    public Optional<ProductDatabaseEntity> getProduct(UUID uuid, UUID accountId) {
        return productsRepository.findByIdAndAccountId(uuid, accountId);
    }

    public List<ProductDatabaseEntity> getProducts(UUID accountId) {
        return productsRepository.findAllByAccountId(accountId);
    }

    public List<ProductDatabaseEntity> getProducts() {
        return productsRepository.findAll();
    }
}
