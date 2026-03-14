package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Kafka event DTO published when a product transitions to PUBLISHED state.
 * Carries all fields needed by the Catalog module to create a CatalogItem
 * without requiring a synchronous callback to the Product service.
 */
public class ProductPublishedEvent implements Serializable {

    public static final String EVENT_TYPE = "PRODUCT_PUBLISHED";

    private String productId;
    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp;

    public ProductPublishedEvent() {
    }

    public ProductPublishedEvent(String productId) {
        this.productId = productId;
        this.timestamp = LocalDateTime.now();
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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
