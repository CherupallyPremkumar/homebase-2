package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Event published when stock is returned to the supplier.
 */
public class StockReturnedToSupplierEvent implements Serializable {

    public static final String EVENT_TYPE = "STOCK_RETURNED_TO_SUPPLIER";

    private String inventoryId;
    private String productId;
    private int returnedQuantity;
    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp = LocalDateTime.now();

    public StockReturnedToSupplierEvent() {
    }

    public StockReturnedToSupplierEvent(String inventoryId, String productId, int returnedQuantity) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.returnedQuantity = returnedQuantity;
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

    public int getReturnedQuantity() {
        return returnedQuantity;
    }

    public void setReturnedQuantity(int returnedQuantity) {
        this.returnedQuantity = returnedQuantity;
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
