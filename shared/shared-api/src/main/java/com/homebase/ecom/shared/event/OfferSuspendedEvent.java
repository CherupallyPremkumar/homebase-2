package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Kafka event published when an offer transitions to SUSPENDED state.
 * Consumed by Notification BC to notify the seller.
 */
public class OfferSuspendedEvent implements Serializable {

    public static final String EVENT_TYPE = "OFFER_SUSPENDED";

    private String offerId;
    private String productId;
    private String supplierId;
    private String reason;
    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp = LocalDateTime.now();

    public OfferSuspendedEvent() {
    }

    public OfferSuspendedEvent(String offerId, String productId, String supplierId, String reason) {
        this.offerId = offerId;
        this.productId = productId;
        this.supplierId = supplierId;
        this.reason = reason;
    }

    public String getOfferId() { return offerId; }
    public void setOfferId(String offerId) { this.offerId = offerId; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
