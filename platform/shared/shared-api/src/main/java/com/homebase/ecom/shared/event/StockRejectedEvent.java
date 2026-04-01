package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Event published when incoming stock is rejected.
 */
public class StockRejectedEvent implements Serializable {

    public static final String EVENT_TYPE = "STOCK_REJECTED";

    private String inventoryId;
    private String productId;
    private String reason;
    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp = LocalDateTime.now();

    public StockRejectedEvent() {
    }

    public StockRejectedEvent(String inventoryId, String productId, String reason) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.reason = reason;
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
