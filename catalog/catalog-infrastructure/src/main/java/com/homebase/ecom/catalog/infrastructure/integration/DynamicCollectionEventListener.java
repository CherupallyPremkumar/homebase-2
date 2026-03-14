package com.homebase.ecom.catalog.infrastructure.integration;

import com.homebase.ecom.catalog.domain.port.in.EvaluateDynamicCollectionUseCase;
import org.springframework.context.event.EventListener;

public class DynamicCollectionEventListener {

    private final EvaluateDynamicCollectionUseCase useCase;

    public DynamicCollectionEventListener(EvaluateDynamicCollectionUseCase useCase) {
        this.useCase = useCase;
    }

    /**
     * Triggered when a new dynamic collection is created or an existing rule is updated.
     */
    @EventListener
    public void onCollectionRulesUpdated(CollectionRulesUpdatedEvent event) {
        useCase.evaluateCollectionForAllItems(event.getCollectionId());
    }

    /**
     * Triggered when a product/offer is updated, meaning it might now match (or fail) active collection rules.
     */
    @EventListener
    public void onProductMetadataUpdated(ProductMetadataUpdatedEvent event) {
        useCase.evaluateItemForAllCollections(event.getProductId());
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
