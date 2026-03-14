package com.homebase.ecom.catalog.infrastructure.persistence.adapter;

import com.homebase.ecom.catalog.model.CollectionProductMapping;
import com.homebase.ecom.catalog.repository.CollectionProductMappingRepository;
import com.homebase.ecom.catalog.infrastructure.persistence.entity.CollectionProductMappingEntity;
import com.homebase.ecom.catalog.infrastructure.persistence.mapper.CatalogMapper;
import com.homebase.ecom.catalog.infrastructure.persistence.repository.CollectionProductMappingJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CollectionProductMappingRepositoryImpl implements CollectionProductMappingRepository {

    @Autowired
    private CollectionProductMappingJpaRepository jpaRepository;

    @Autowired
    private CatalogMapper mapper;

    @Override
    public void deleteByCollectionId(String collectionId) {
        jpaRepository.deleteByCollectionId(collectionId);
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<CollectionProductMapping> findByCollectionId(String collectionId) {
        return jpaRepository.findByCollectionId(collectionId).stream()
                .map(mapper::toCollectionProductMappingDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CollectionProductMapping> findByCollectionAndProduct(String collectionId, String productId) {
        return jpaRepository.findByCollectionIdAndProductId(collectionId, productId)
                .map(mapper::toCollectionProductMappingDomain);
    }

    @Override
    public CollectionProductMapping save(CollectionProductMapping mapping) {
        CollectionProductMappingEntity entity = mapper.toCollectionProductMappingEntity(mapping);
        CollectionProductMappingEntity saved = jpaRepository.save(entity);
        return mapper.toCollectionProductMappingDomain(saved);
    }

    @Override
    public void saveAll(List<CollectionProductMapping> mappings) {
        List<CollectionProductMappingEntity> entities = mappings.stream()
                .map(mapper::toCollectionProductMappingEntity)
                .collect(Collectors.toList());
        jpaRepository.saveAll(entities);
    }
}
