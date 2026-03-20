package com.homebase.ecom.catalog.infrastructure.persistence.adapter;

import com.homebase.ecom.catalog.model.CatalogItem;
import com.homebase.ecom.catalog.repository.CatalogItemRepository;
import com.homebase.ecom.catalog.infrastructure.persistence.entity.CatalogItemEntity;
import com.homebase.ecom.catalog.infrastructure.persistence.mapper.CatalogMapper;
import com.homebase.ecom.catalog.infrastructure.persistence.repository.CatalogItemJpaRepository;
import java.util.List;
import java.util.Optional;

public class CatalogItemRepositoryImpl implements CatalogItemRepository {

    private final CatalogItemJpaRepository jpaRepository;
    private final CatalogMapper mapper;

    public CatalogItemRepositoryImpl(CatalogItemJpaRepository jpaRepository, CatalogMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<CatalogItem> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<CatalogItem> findByProductId(String productId) {
        return jpaRepository.findByProductId(productId).map(mapper::toDomain);
    }

    @Override
    public List<CatalogItem> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public CatalogItem save(CatalogItem catalogItem) {
        CatalogItemEntity entity = mapper.toEntity(catalogItem);
        CatalogItemEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public int countByFeaturedTrue() {
        return jpaRepository.countByFeaturedTrue();
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }
}
