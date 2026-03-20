package com.homebase.ecom.catalog.port.in;

import com.homebase.ecom.catalog.model.CatalogItem;
import com.homebase.ecom.shared.event.ProductPublishedEvent;

public interface CatalogService {
    CatalogItem createOrUpdateCatalogItem(ProductPublishedEvent event);
    void updateVisibility(String productId, int newQuantity);
}
