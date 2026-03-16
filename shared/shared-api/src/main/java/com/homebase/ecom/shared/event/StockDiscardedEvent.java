package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Event published when damaged or expired stock is discarded.
 */
public class StockDiscardedEvent implements Serializable {

    public static final String EVENT_TYPE = "STOCK_DISCARDED";

    private String inventoryId;
    private String productId;
    private int discardedQuantity;
    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp = LocalDateTime.now();

    public StockDiscardedEvent() {
    }

    public StockDiscardedEvent(String inventoryId, String productId, int discardedQuantity) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.discardedQuantity = discardedQuantity;
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

    public int getDiscardedQuantity() {
        return discardedQuantity;
    }

    public void setDiscardedQuantity(int discardedQuantity) {
        this.discardedQuantity = discardedQuantity;
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
