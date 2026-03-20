package com.homebase.ecom.catalog.infrastructure.persistence.adapter;

import com.homebase.ecom.catalog.model.CategoryProductMapping;
import com.homebase.ecom.catalog.repository.CategoryProductMappingRepository;
import com.homebase.ecom.catalog.infrastructure.persistence.entity.CategoryProductMappingEntity;
import com.homebase.ecom.catalog.infrastructure.persistence.mapper.CatalogMapper;
import com.homebase.ecom.catalog.infrastructure.persistence.repository.CategoryProductMappingJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CategoryProductMappingRepositoryImpl implements CategoryProductMappingRepository {

    private final CategoryProductMappingJpaRepository jpaRepository;
    private final CatalogMapper mapper;

    public CategoryProductMappingRepositoryImpl(CategoryProductMappingJpaRepository jpaRepository, CatalogMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<CategoryProductMapping> findByCategoryIdOrderByDisplayOrderAsc(String categoryId) {
        return jpaRepository.findByCategoryIdOrderByDisplayOrderAsc(categoryId).stream()
                .map(mapper::toCategoryProductMappingDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CategoryProductMapping> findByCategoryIdAndProductId(String categoryId, String productId) {
        return jpaRepository.findByCategoryIdAndProductId(categoryId, productId).map(mapper::toCategoryProductMappingDomain);
    }

    @Override
    public List<CategoryProductMapping> findByProductId(String productId) {
        return jpaRepository.findByProductId(productId).stream()
                .map(mapper::toCategoryProductMappingDomain)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryProductMapping save(CategoryProductMapping mapping) {
        CategoryProductMappingEntity entity = mapper.toCategoryProductMappingEntity(mapping);
        CategoryProductMappingEntity saved = jpaRepository.save(entity);
        return mapper.toCategoryProductMappingDomain(saved);
    }

    @Override
    public void deleteByCategoryIdAndProductId(String categoryId, String productId) {
        jpaRepository.deleteByCategoryIdAndProductId(categoryId, productId);
    }
}
