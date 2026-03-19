package com.homebase.ecom.catalog.service.event;

import com.homebase.ecom.catalog.port.in.CatalogService;
import com.homebase.ecom.catalog.port.in.UpdateCatalogUseCase;
import com.homebase.ecom.catalog.port.in.EvaluateDynamicCollectionUseCase;
import com.homebase.ecom.shared.event.ProductPublishedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

/**
 * Driving adapter: listens to domain events and delegates to catalog use cases.
 */
public class CatalogEventHandler {

    private static final Logger log = LoggerFactory.getLogger(CatalogEventHandler.class);

    @Autowired(required = false)
    private CatalogService catalogService;

    @Autowired(required = false)
    private UpdateCatalogUseCase updateCatalogUseCase;

    @Autowired(required = false)
    private EvaluateDynamicCollectionUseCase dynamicCollectionUseCase;

    @EventListener
    public void onProductPublished(ProductPublishedEvent event) {
        if (event.getProductId() == null) return;
        try {
            if (catalogService != null) {
                catalogService.createOrUpdateCatalogItem(event);
            }
            if (dynamicCollectionUseCase != null) {
                dynamicCollectionUseCase.evaluateItemForAllCollections(event.getProductId());
            }
        } catch (Exception e) {
            log.warn("Error processing ProductPublishedEvent for product {}: {}",
                    event.getProductId(), e.getMessage());
        }
    }
}
