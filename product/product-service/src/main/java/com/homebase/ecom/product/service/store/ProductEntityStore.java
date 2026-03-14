package com.homebase.ecom.product.service.store;

import org.chenile.utils.entity.service.EntityStore;
import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.domain.port.ProductRepository;
import org.chenile.base.exception.NotFoundException;
import java.util.Optional;

public class ProductEntityStore implements EntityStore<Product>{
    private final ProductRepository productRepository;

    public ProductEntityStore(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

	@Override
	public void store(Product entity) {
        productRepository.save(entity);
	}

	@Override
	public Product retrieve(String id) {
        Optional<Product> entity = productRepository.findById(id);
        if (entity.isPresent()) return entity.get();
        throw new NotFoundException(1500,"Unable to find Product with ID " + id);
	}

}
