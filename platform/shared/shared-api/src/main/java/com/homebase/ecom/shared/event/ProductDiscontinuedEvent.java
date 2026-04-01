package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Kafka event DTO published when a product transitions to DISCONTINUED state.
 * The Catalog module consumes this to permanently remove the product from listings.
 */
public class ProductDiscontinuedEvent implements Serializable {

    public static final String EVENT_TYPE = "PRODUCT_DISCONTINUED";

    private String productId;
    private String reason;
    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp;

    public ProductDiscontinuedEvent() {
    }

    public ProductDiscontinuedEvent(String productId, String reason) {
        this.productId = productId;
        this.reason = reason;
        this.timestamp = LocalDateTime.now();
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
