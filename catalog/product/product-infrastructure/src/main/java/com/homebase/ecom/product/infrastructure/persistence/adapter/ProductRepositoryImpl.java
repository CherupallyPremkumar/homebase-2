package com.homebase.ecom.product.infrastructure.persistence.adapter;

import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.domain.port.ProductRepository;
import com.homebase.ecom.product.infrastructure.persistence.mapper.ProductMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        var entity = jpaRepository.save(mapper.toEntity(product));
        product.setId(entity.getId());
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Product> findBySupplierIdAndStateId(String supplierId, String stateId) {
        return jpaRepository.findBySupplierIdAndStateId(supplierId, stateId).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findBySupplierId(String supplierId) {
        return jpaRepository.findBySupplierId(supplierId).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }
}
