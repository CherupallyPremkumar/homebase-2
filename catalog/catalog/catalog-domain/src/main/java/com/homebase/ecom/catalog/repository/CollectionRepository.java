package com.homebase.ecom.catalog.repository;

import com.homebase.ecom.catalog.model.Collection;
import java.util.List;
import java.util.Optional;

public interface CollectionRepository {
    Optional<Collection> findById(String id);
    List<Collection> findAllActiveDynamicCollections();
    List<Collection> findDynamicCollections();
    List<Collection> findByActiveTrue();
    int countByFeaturedTrue();
    Collection save(Collection collection);
    long count();
    void deleteById(String id);
}
