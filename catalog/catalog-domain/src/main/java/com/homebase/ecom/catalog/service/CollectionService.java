package com.homebase.ecom.catalog.service;

import com.homebase.ecom.catalog.model.Collection;
import java.util.List;
import java.util.Optional;

public interface CollectionService {
    Collection createCollection(Collection collection);
    Collection updateCollection(String id, Collection collection);
    Optional<Collection> getCollectionById(String id);
    List<Collection> getActiveCollections();
    List<Collection> getDynamicCollections();
    void deleteCollection(String id);
}
