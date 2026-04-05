package com.homebase.ecom.cart.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Base class for all cart domain events.
 * Each subclass represents a specific lifecycle transition.
 * Adapters serialize this to JSON and publish to "cart.events" topic.
 */
public abstract class CartEvent implements Serializable {

    private final String eventType;
    private final String cartId;
    private final LocalDateTime timestamp;

    protected CartEvent(String eventType, String cartId, LocalDateTime timestamp) {
        this.eventType = eventType;
        this.cartId = cartId;
        this.timestamp = timestamp;
    }

    public String getEventType() {
        return eventType;
    }

    public String getCartId() {
        return cartId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
