package com.homebase.ecom.catalog.infrastructure.persistence.adapter;

import com.homebase.ecom.catalog.model.Category;
import com.homebase.ecom.catalog.repository.CategoryRepository;
import com.homebase.ecom.catalog.infrastructure.persistence.entity.CategoryEntity;
import com.homebase.ecom.catalog.infrastructure.persistence.mapper.CatalogMapper;
import com.homebase.ecom.catalog.infrastructure.persistence.repository.CatalogCategoryJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CategoryRepositoryImpl implements CategoryRepository {

    private final CatalogCategoryJpaRepository jpaRepository;
    private final CatalogMapper mapper;

    public CategoryRepositoryImpl(CatalogCategoryJpaRepository jpaRepository, CatalogMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Category> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toCategoryDomain);
    }

    @Override
    public List<Category> findRootCategories() {
        return jpaRepository.findRootCategories().stream()
                .map(mapper::toCategoryDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> findByParentId(String parentId) {
        return jpaRepository.findByParentId(parentId).stream()
                .map(mapper::toCategoryDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> findByActiveTrue() {
        return jpaRepository.findByActiveTrue().stream()
                .map(mapper::toCategoryDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Category save(Category category) {
        CategoryEntity entity = mapper.toCategoryEntity(category);
        CategoryEntity saved = jpaRepository.save(entity);
        return mapper.toCategoryDomain(saved);
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }
}
