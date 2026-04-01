package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Kafka event DTO published when a product transitions to DISABLED state.
 * The Catalog module consumes this to hide the product from storefront listings.
 */
public class ProductDisabledEvent implements Serializable {

    public static final String EVENT_TYPE = "PRODUCT_DISABLED";

    private String productId;
    private String reason;
    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp;

    public ProductDisabledEvent() {
    }

    public ProductDisabledEvent(String productId, String reason) {
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
