package com.homebase.ecom.catalog.service.listener;

import com.homebase.ecom.product.event.InventoryChangedEvent;
import com.homebase.ecom.product.event.ProductApprovedEvent;
import com.homebase.ecom.catalog.domain.port.in.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class ProductEventListener {

    @Autowired
    private CatalogService catalogService;

    @EventListener
    public void onProductApproved(ProductApprovedEvent event) {
        // "Upsert" logic to handle duplicate events
        // Using idempotent service method
        if (event.getProductId() != null) {
            com.homebase.ecom.shared.event.ProductPublishedEvent publishedEvent = 
                new com.homebase.ecom.shared.event.ProductPublishedEvent(event.getProductId());
            catalogService.createOrUpdateCatalogItem(publishedEvent);
        }
    }

    @EventListener
    public void onProductInventoryChanged(InventoryChangedEvent event) {
        if (event.getProductId() != null) {
            catalogService.updateVisibility(event.getProductId(), event.getNewQuantity());
        }
    }
}
