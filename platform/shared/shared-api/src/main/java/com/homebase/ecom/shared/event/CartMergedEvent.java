package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Kafka event DTO published when a guest cart is merged into an authenticated user's cart.
 * Used for analytics and releasing reservations on the source (guest) cart.
 */
public class CartMergedEvent implements Serializable {

    public static final String EVENT_TYPE = "CART_MERGED";

    private String sourceCartId;
    private String targetCartId;
    private String customerId;
    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp;

    public CartMergedEvent() {
    }

    public CartMergedEvent(String sourceCartId, String targetCartId, String customerId, LocalDateTime timestamp) {
        this.sourceCartId = sourceCartId;
        this.targetCartId = targetCartId;
        this.customerId = customerId;
        this.timestamp = timestamp;
    }

    public String getSourceCartId() {
        return sourceCartId;
    }

    public void setSourceCartId(String sourceCartId) {
        this.sourceCartId = sourceCartId;
    }

    public String getTargetCartId() {
        return targetCartId;
    }

    public void setTargetCartId(String targetCartId) {
        this.targetCartId = targetCartId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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
