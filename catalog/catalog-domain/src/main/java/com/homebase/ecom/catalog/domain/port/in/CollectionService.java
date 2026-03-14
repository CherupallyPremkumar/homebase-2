package com.homebase.ecom.catalog.domain.port.in;

import com.homebase.ecom.catalog.model.Collection;
import java.util.List;
import java.util.Optional;

public interface CollectionService {
    /**
     * Create a new collection
     */
    Collection createCollection(Collection collection);

    /**
     * Update an existing collection
     */
    Collection updateCollection(String id, Collection collection);

    /**
     * Get collection by ID
     */
    Optional<Collection> getCollectionById(String id);

    /**
     * Get all active collections
     */
    List<Collection> getActiveCollections();

    /**
     * Get all dynamic collections (for scheduler)
     */
    List<Collection> getDynamicCollections();

    /**
     * Delete a collection (soft delete - set active=false)
     */
    void deleteCollection(String id);
}
