package com.homebase.ecom.catalog.repository;

import com.homebase.ecom.product.dto.ProductCatalogDetails;
import java.util.Optional;
import java.util.List;

/**
 * Port for fetching external product details from the Product module.
 * This represents a "Driven Port" in the Hexagonal architecture.
 */
public interface ProductServiceClient {
    Optional<ProductCatalogDetails> getProduct(String productId);
    List<ProductCatalogDetails> getAllProducts();
}
