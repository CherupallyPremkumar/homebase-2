package com.homebase.ecom.catalog.repository;

import com.homebase.ecom.catalog.model.CollectionProductMapping;
import java.util.List;
import java.util.Optional;

public interface CollectionProductMappingRepository {
    void deleteByCollectionId(String collectionId);
    void deleteById(String id);
    List<CollectionProductMapping> findByCollectionId(String collectionId);
    Optional<CollectionProductMapping> findByCollectionAndProduct(String collectionId, String productId);
    CollectionProductMapping save(CollectionProductMapping mapping);
    void saveAll(List<CollectionProductMapping> mappings);
}
