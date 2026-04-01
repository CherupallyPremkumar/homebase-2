package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Kafka event DTO published when a new cart is created.
 * Used for analytics tracking (e.g., funnel analysis, session tracking).
 */
public class CartCreatedEvent implements Serializable {

    public static final String EVENT_TYPE = "CART_CREATED";

    private String cartId;
    private String userId;
    private String source;
    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp;

    public CartCreatedEvent() {
    }

    public CartCreatedEvent(String cartId, String userId, String source, LocalDateTime timestamp) {
        this.cartId = cartId;
        this.userId = userId;
        this.source = source;
        this.timestamp = timestamp;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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
