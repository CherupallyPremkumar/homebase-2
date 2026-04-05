package com.homebase.ecom.catalog.infrastructure.integration;

import com.homebase.ecom.catalog.port.client.ProductDataPort;
import com.homebase.ecom.catalog.port.client.ProductSnapshot;
import com.homebase.ecom.product.api.ProductService;
import com.homebase.ecom.product.model.Product;
import org.chenile.query.service.SearchService;

import java.util.List;
import java.util.Optional;

/**
 * Driven adapter: translates Product bounded context → catalog's ProductSnapshot (ACL).
 * Uses product-client proxy (via Chenile ProxyBuilder).
 */
public class ProductServiceAdapter implements ProductDataPort {

    private final ProductService productService;
    private final SearchService productSearchService;

    public ProductServiceAdapter(ProductService productService, SearchService productSearchService) {
        this.productService = productService;
        this.productSearchService = productSearchService;
    }

    @Override
    public Optional<ProductSnapshot> getProduct(String productId) {
        try {
            Product product = productService.retrieve(productId);
            if (product == null) {
                return Optional.empty();
            }
            return Optional.of(toSnapshot(product));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private ProductSnapshot toSnapshot(Product product) {
        ProductSnapshot snapshot = new ProductSnapshot();
        snapshot.setId(product.getId());
        snapshot.setName(product.getName());
        snapshot.setSlug(product.getSlug());
        snapshot.setDescription(product.getDescription());
        snapshot.setBrand(product.getBrand());
        snapshot.setCategory(product.getCategoryId());
        snapshot.setActive(true);
        snapshot.setCategoryIds(product.getCategoryId() != null ? List.of(product.getCategoryId()) : List.of());
        return snapshot;
    }
}
