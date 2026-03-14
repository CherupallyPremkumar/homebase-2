package com.homebase.ecom.product.service.impl;

import com.homebase.ecom.product.api.ProductService;
import com.homebase.ecom.product.model.Product;
import com.homebase.ecom.product.domain.port.ProductRepository;
import com.homebase.ecom.product.infrastructure.persistence.mapper.ProductMapper;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.chenile.workflow.dto.StateEntityServiceResponse;

public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final StateEntityServiceImpl<com.homebase.ecom.product.domain.model.Product> stateEntityService;

    public ProductServiceImpl(ProductRepository productRepository, 
                              ProductMapper productMapper,
                              StateEntityServiceImpl<com.homebase.ecom.product.domain.model.Product> stateEntityService) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.stateEntityService = stateEntityService;
    }

    @Override
    public Product create(Product productDto) {
        com.homebase.ecom.product.domain.model.Product domain = toDomain(productDto);
        productRepository.save(domain);
        return toDto(domain);
    }

    @Override
    public Product retrieve(String id) {
        return productRepository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public Product update(String id, Product productDto) {
        // Implementation
        return null;
    }

    @Override
    public void delete(String id) {
        productRepository.delete(id);
    }

    @Override
    public Product processById(String id, String eventId, Object payload) {
        StateEntityServiceResponse<com.homebase.ecom.product.domain.model.Product> response = 
            stateEntityService.processById(id, eventId, payload);
        return toDto(response.getMutatedEntity());
    }

    private Product toDto(com.homebase.ecom.product.domain.model.Product domain) {
        if (domain == null) return null;
        Product dto = new Product();
        dto.setId(domain.getId());
        dto.setName(domain.getName());
        dto.setDescription(domain.getDescription());
        dto.setCategoryId(domain.getCategoryId());
        dto.setBrand(domain.getBrand());
        dto.setCurrentState(domain.getCurrentState());
        return dto;
    }

    private com.homebase.ecom.product.domain.model.Product toDomain(Product dto) {
        if (dto == null) return null;
        com.homebase.ecom.product.domain.model.Product domain = new com.homebase.ecom.product.domain.model.Product();
        domain.setId(dto.getId());
        domain.setName(dto.getName());
        domain.setDescription(dto.getDescription());
        domain.setCategoryId(dto.getCategoryId());
        domain.setBrand(dto.getBrand());
        domain.setCurrentState(dto.getCurrentState());
        return domain;
    }
}
