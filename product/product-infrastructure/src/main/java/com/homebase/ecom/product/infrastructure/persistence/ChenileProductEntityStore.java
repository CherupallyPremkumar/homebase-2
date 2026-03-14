package com.homebase.ecom.product.infrastructure.persistence;

import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.domain.port.ProductRepository;
import org.chenile.utils.entity.service.EntityStore;

public class ChenileProductEntityStore implements EntityStore<Product> {

    private final ProductRepository productRepository;

    public ChenileProductEntityStore(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void store(Product product) {
        productRepository.save(product);
    }

    @Override
    public Product retrieve(String id) {
        return productRepository.findById(id).orElse(null);
    }
}
