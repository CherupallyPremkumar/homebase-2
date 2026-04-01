package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Kafka event DTO published when a cart is abandoned.
 * Used for releasing early inventory reservations.
 */
public class CartAbandonedEvent implements Serializable {

    public static final String EVENT_TYPE = "CART_ABANDONED";

    private String cartId;
    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp;

    public CartAbandonedEvent() {
    }

    public CartAbandonedEvent(String cartId, LocalDateTime timestamp) {
        this.cartId = cartId;
        this.timestamp = timestamp;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
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
