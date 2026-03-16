package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Kafka event published when an offer transitions to EXPIRED state.
 * Consumed by Pricing BC to revert catalog pricing.
 */
public class OfferExpiredEvent implements Serializable {

    public static final String EVENT_TYPE = "OFFER_EXPIRED";

    private String offerId;
    private String productId;
    private String supplierId;
    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp = LocalDateTime.now();

    public OfferExpiredEvent() {
    }

    public OfferExpiredEvent(String offerId, String productId, String supplierId) {
        this.offerId = offerId;
        this.productId = productId;
        this.supplierId = supplierId;
    }

    public String getOfferId() { return offerId; }
    public void setOfferId(String offerId) { this.offerId = offerId; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
