package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ProductInventoryInitializedEvent implements Serializable {
    public static final String EVENT_TYPE = "PRODUCT_INVENTORY_INITIALIZED";

    private String productId;
    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp;

    public ProductInventoryInitializedEvent() {
    }

    public ProductInventoryInitializedEvent(String productId, LocalDateTime timestamp) {
        this.productId = productId;
        this.timestamp = timestamp;
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
