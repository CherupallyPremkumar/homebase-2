package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Generic event for inventory-related signals.
 */
public class InventoryEvent implements Serializable {

    public static final String STOCK_RESERVED = "STOCK_RESERVED";
    public static final String STOCK_FAILED = "STOCK_FAILED";
    public static final String RELEASE_STOCK = "RELEASE_STOCK";
    public static final String STOCK_RELEASED = "STOCK_RELEASED";

    private String orderId;
    private String eventType;
    private LocalDateTime timestamp = LocalDateTime.now();

    public InventoryEvent() {
    }

    public InventoryEvent(String orderId, String eventType) {
        this.orderId = orderId;
        this.eventType = eventType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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
