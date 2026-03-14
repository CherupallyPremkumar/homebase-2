package com.homebase.ecom.catalog.infrastructure.integration;

import com.homebase.ecom.catalog.repository.ProductServiceClient;
import com.homebase.ecom.product.dto.ProductCatalogDetails;
import com.homebase.ecom.shared.Money;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;
import java.util.Arrays;

/**
 * Implementation of ProductServiceClient moved to Infrastructure.
 * This is an Adapter that simulates calling the Product module.
 */
@Service
public class ProductServiceClientImpl implements ProductServiceClient {

    @Override
    public Optional<ProductCatalogDetails> getProduct(String productId) {
        ProductCatalogDetails details = new ProductCatalogDetails();
        details.setProductId(productId);
        details.setName("Simulated Product " + productId);
        details.setPrice(new Money(new BigDecimal("49.99"), "USD"));
        details.setActive(true);
        details.setCategory("Default");
        return Optional.of(details);
    }

    @Override
    public List<ProductCatalogDetails> getAllProducts() {
        return Arrays.asList(
            createDetails("prod-001", "Summer Dress", "45.00"),
            createDetails("prod-002", "Winter Jacket", "150.00"),
            createDetails("prod-003", "Cheap Accessories", "10.00"),
            createDetails("prod-004", "Premium Handbag", "500.00")
        );
    }

    private ProductCatalogDetails createDetails(String id, String name, String price) {
        ProductCatalogDetails d = new ProductCatalogDetails();
        d.setProductId(id);
        d.setName(name);
        d.setPrice(new Money(new BigDecimal(price), "USD"));
        d.setActive(true);
        d.setCategory("Clothing");
        return d;
    }
}
