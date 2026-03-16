package com.homebase.ecom.catalog.service.listener;

import com.homebase.ecom.product.event.InventoryChangedEvent;
import com.homebase.ecom.product.event.ProductApprovedEvent;
import com.homebase.ecom.catalog.domain.port.in.CatalogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class ProductEventListener {

    private static final Logger log = LoggerFactory.getLogger(ProductEventListener.class);

    @Autowired
    private CatalogService catalogService;

    @EventListener
    public void onProductApproved(ProductApprovedEvent event) {
        // "Upsert" logic to handle duplicate events
        // Using idempotent service method
        if (event.getProductId() != null) {
            try {
                com.homebase.ecom.shared.event.ProductPublishedEvent publishedEvent =
                    new com.homebase.ecom.shared.event.ProductPublishedEvent(event.getProductId());
                catalogService.createOrUpdateCatalogItem(publishedEvent);
            } catch (Exception e) {
                log.warn("Idempotency: error processing ProductApprovedEvent for product {} (possible replay). Skipping. Detail: {}",
                        event.getProductId(), e.getMessage());
            }
        }
    }

    @EventListener
    public void onProductInventoryChanged(InventoryChangedEvent event) {
        if (event.getProductId() != null) {
            try {
                catalogService.updateVisibility(event.getProductId(), event.getNewQuantity());
            } catch (Exception e) {
                log.warn("Idempotency: error processing InventoryChangedEvent for product {} (possible replay). Skipping. Detail: {}",
                        event.getProductId(), e.getMessage());
            }
        }
    }
}
