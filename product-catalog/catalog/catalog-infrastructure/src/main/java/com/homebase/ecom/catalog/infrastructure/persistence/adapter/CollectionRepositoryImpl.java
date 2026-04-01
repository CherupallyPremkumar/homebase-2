package com.homebase.ecom.catalog.infrastructure.persistence.adapter;

import com.homebase.ecom.catalog.model.Collection;
import com.homebase.ecom.catalog.repository.CollectionRepository;
import com.homebase.ecom.catalog.infrastructure.persistence.entity.CollectionEntity;
import com.homebase.ecom.catalog.infrastructure.persistence.mapper.CatalogMapper;
import com.homebase.ecom.catalog.infrastructure.persistence.repository.CollectionJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CollectionRepositoryImpl implements CollectionRepository {

    private final CollectionJpaRepository jpaRepository;
    private final CatalogMapper mapper;

    public CollectionRepositoryImpl(CollectionJpaRepository jpaRepository, CatalogMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Collection> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toCollectionDomain);
    }

    @Override
    public List<Collection> findAllActiveDynamicCollections() {
        return jpaRepository.findAllActiveDynamicCollections().stream()
                .map(mapper::toCollectionDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Collection> findDynamicCollections() {
        return jpaRepository.findDynamicCollections().stream()
                .map(mapper::toCollectionDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Collection> findByActiveTrue() {
        return jpaRepository.findByActiveTrue().stream()
                .map(mapper::toCollectionDomain)
                .collect(Collectors.toList());
    }

    @Override
    public int countByFeaturedTrue() {
        return jpaRepository.countByFeaturedTrue();
    }

    @Override
    public Collection save(Collection collection) {
        CollectionEntity entity = mapper.toCollectionEntity(collection);
        CollectionEntity saved = jpaRepository.save(entity);
        return mapper.toCollectionDomain(saved);
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
