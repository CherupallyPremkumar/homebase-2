package com.homebase.ecom.catalog.infrastructure.integration;

import com.homebase.ecom.catalog.domain.port.in.EvaluateDynamicCollectionUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;

public class DynamicCollectionEventListener {

    private static final Logger log = LoggerFactory.getLogger(DynamicCollectionEventListener.class);

    private final EvaluateDynamicCollectionUseCase useCase;

    public DynamicCollectionEventListener(EvaluateDynamicCollectionUseCase useCase) {
        this.useCase = useCase;
    }

    /**
     * Triggered when a new dynamic collection is created or an existing rule is updated.
     */
    @EventListener
    public void onCollectionRulesUpdated(CollectionRulesUpdatedEvent event) {
        try {
            useCase.evaluateCollectionForAllItems(event.getCollectionId());
        } catch (Exception e) {
            log.warn("Idempotency: error processing CollectionRulesUpdatedEvent for collection {} (possible replay). Skipping. Detail: {}",
                    event.getCollectionId(), e.getMessage());
        }
    }

    /**
     * Triggered when a product/offer is updated, meaning it might now match (or fail) active collection rules.
     */
    @EventListener
    public void onProductMetadataUpdated(ProductMetadataUpdatedEvent event) {
        try {
            useCase.evaluateItemForAllCollections(event.getProductId());
        } catch (Exception e) {
            log.warn("Idempotency: error processing ProductMetadataUpdatedEvent for product {} (possible replay). Skipping. Detail: {}",
                    event.getProductId(), e.getMessage());
        }
    }

    // Temporary internal event DTOs until cross-module event bus is formalized
    public static class CollectionRulesUpdatedEvent {
        private String collectionId;
        public CollectionRulesUpdatedEvent(String collectionId) { this.collectionId = collectionId; }
        public String getCollectionId() { return collectionId; }
    }

    public static class ProductMetadataUpdatedEvent {
        private String productId;
        public ProductMetadataUpdatedEvent(String productId) { this.productId = productId; }
        public String getProductId() { return productId; }
    }
}
