package com.homebase.ecom.product.infrastructure.persistence.adapter;

import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.domain.port.ProductRepository;
import com.homebase.ecom.product.infrastructure.persistence.entity.ProductEntity;
import com.homebase.ecom.product.infrastructure.persistence.mapper.ProductMapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository jpaRepository;
    private final ProductMapper mapper;

    public ProductRepositoryImpl(ProductJpaRepository jpaRepository, ProductMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Product> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public void save(Product product) {
        jpaRepository.save(mapper.toEntity(product));
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }

    public interface ProductJpaRepository extends JpaRepository<ProductEntity, String> {
    }
}
