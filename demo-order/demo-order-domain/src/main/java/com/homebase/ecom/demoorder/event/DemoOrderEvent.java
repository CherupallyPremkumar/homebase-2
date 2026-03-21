package com.homebase.ecom.demoorder.event;

import java.time.LocalDateTime;

/**
 * Base event for demo order domain events.
 */
public class DemoOrderEvent {

    private String eventType;
    private String orderId;
    private LocalDateTime timestamp;

    public DemoOrderEvent() {}

    public DemoOrderEvent(String eventType, String orderId, LocalDateTime timestamp) {
        this.eventType = eventType;
        this.orderId = orderId;
        this.timestamp = timestamp;
    }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
