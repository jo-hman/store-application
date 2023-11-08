package org.jochmen.product.controller;

import org.jochmen.product.controller.schema.request.ProductCreationRequest;
import org.jochmen.product.controller.schema.response.ProductIdResponse;
import org.jochmen.product.controller.schema.response.ProductResponse;
import org.jochmen.product.repository.ProductDatabaseEntity;
import org.jochmen.product.service.ProductsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@RestController
@RequestMapping("/products")
public class ProductsController {

    private final static String ACCOUNT_HEADER = "account-header";

    private final ProductsService productsService;

    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts(@RequestHeader(ACCOUNT_HEADER) String accountId) {
        return ResponseEntity.ok(productsService.getProducts(UUID.fromString(accountId)).stream()
                .map(mapProductDatabaseEntityToProductResponse())
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("id") UUID uuid,
                                                      @RequestHeader(ACCOUNT_HEADER) String accountId) {
        return productsService.getProduct(uuid, UUID.fromString(accountId))
                .map(mapProductDatabaseEntityToProductResponse())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductIdResponse> createProduct(@RequestBody ProductCreationRequest productCreationRequest,
                                                           @RequestHeader(ACCOUNT_HEADER) String accountId) {
        return productsService.createProduct(productCreationRequest, UUID.fromString(accountId))
                .map(ProductIdResponse::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    private static Function<ProductDatabaseEntity, ProductResponse> mapProductDatabaseEntityToProductResponse() {
        return productDatabaseEntity -> new ProductResponse(productDatabaseEntity.getId(), productDatabaseEntity.getName());
    }

}
