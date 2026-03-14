package com.homebase.ecom.catalog.repository;

import com.homebase.ecom.catalog.model.CatalogItem;
import java.util.Optional;

public interface CatalogItemRepository {
    Optional<CatalogItem> findById(String id);
    Optional<CatalogItem> findByProductId(String productId);
    CatalogItem save(CatalogItem catalogItem);
    long count();
    void deleteById(String id);
    int countByFeaturedTrue();
}
