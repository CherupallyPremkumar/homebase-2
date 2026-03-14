package com.homebase.ecom.catalog.infrastructure.integration;

import com.homebase.ecom.catalog.domain.port.in.UpdateCatalogUseCase;
import org.springframework.context.event.EventListener;

public class OfferEventConsumer {

    private final UpdateCatalogUseCase updateCatalogUseCase;

    public OfferEventConsumer(UpdateCatalogUseCase updateCatalogUseCase) {
        this.updateCatalogUseCase = updateCatalogUseCase;
    }

    // Using Spring's generic ApplicationEvents for now.
    // In production, this would be @KafkaListener or Chenile event listener
    @EventListener
    public void handleOfferEvent(OfferActivatedIntegrationEvent event) {
        updateCatalogUseCase.syncCatalogItem(event.getOfferId(), event.getProductId());
    }

    // Temporary DTO for event payload until generic event bus is finalized
    public static class OfferActivatedIntegrationEvent {
        private String offerId;
        private String productId;

        public OfferActivatedIntegrationEvent(String offerId, String productId) {
            this.offerId = offerId;
            this.productId = productId;
        }

        public String getOfferId() { return offerId; }
        public String getProductId() { return productId; }
    }
}
