package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Event published when stock is fully depleted for a product.
 */
public class StockDepletedEvent implements Serializable {

    public static final String EVENT_TYPE = "STOCK_DEPLETED";

    private String inventoryId;
    private String productId;
    private String variantId;
    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp = LocalDateTime.now();

    public StockDepletedEvent() {
    }

    public StockDepletedEvent(String inventoryId, String productId, String variantId) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.variantId = variantId;
    }

    // --- Getters & Setters ---

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getVariantId() {
        return variantId;
    }

    public void setVariantId(String variantId) {
        this.variantId = variantId;
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
