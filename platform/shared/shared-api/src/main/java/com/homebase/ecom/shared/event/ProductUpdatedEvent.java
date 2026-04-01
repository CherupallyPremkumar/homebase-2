package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Kafka event published when a product's pending update is approved.
 * Catalog module consumes this to refresh product data in storefront.
 */
public class ProductUpdatedEvent implements Serializable {

    public static final String EVENT_TYPE = "PRODUCT_UPDATED";

    private String productId;
    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp;

    public ProductUpdatedEvent() {
    }

    public ProductUpdatedEvent(String productId) {
        this.productId = productId;
        this.timestamp = LocalDateTime.now();
    }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
