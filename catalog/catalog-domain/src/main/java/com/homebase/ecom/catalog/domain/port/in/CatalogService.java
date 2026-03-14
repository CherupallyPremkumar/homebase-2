package com.homebase.ecom.catalog.domain.port.in;

import com.homebase.ecom.catalog.model.CatalogItem;
import com.homebase.ecom.shared.event.ProductPublishedEvent;

public interface CatalogService {
    /**
     * Create or update a catalog item based on a published product.
     * Fetches additional product details dynamically from the QueryAdapter.
     * This method is idempotent and safe to call multiple times.
     * 
     * @param event the {@link ProductPublishedEvent} containing the product ID
     * @return the created or updated CatalogItem
     */
    CatalogItem createOrUpdateCatalogItem(ProductPublishedEvent event);

    /**
     * Update the visibility of a catalog item based on inventory quantity.
     * 
     * @param productId   the unique identifier of the product
     * @param newQuantity the new available quantity
     */
    void updateVisibility(String productId, int newQuantity);
}
