package com.homebase.ecom.catalog.infrastructure.persistence.repository;

import com.homebase.ecom.catalog.infrastructure.persistence.entity.CollectionProductMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionProductMappingJpaRepository extends JpaRepository<CollectionProductMappingEntity, String> {
    void deleteByCollectionId(String collectionId);
    List<CollectionProductMappingEntity> findByCollectionId(String collectionId);
    Optional<CollectionProductMappingEntity> findByCollectionIdAndProductId(String collectionId, String productId);
}
