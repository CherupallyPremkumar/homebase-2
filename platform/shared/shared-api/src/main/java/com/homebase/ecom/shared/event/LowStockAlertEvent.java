package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Event published when stock falls below the configured threshold.
 */
public class LowStockAlertEvent implements Serializable {

    public static final String EVENT_TYPE = "LOW_STOCK_ALERT";

    private String inventoryId;
    private String productId;
    private int availableQuantity;
    private int threshold;
    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp = LocalDateTime.now();

    public LowStockAlertEvent() {
    }

    public LowStockAlertEvent(String inventoryId, String productId, int availableQuantity, int threshold) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.availableQuantity = availableQuantity;
        this.threshold = threshold;
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

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
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
