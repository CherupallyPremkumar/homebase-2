package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Kafka event DTO published when a cart expires.
 * Used for releasing any inventory reservations.
 */
public class CartExpiredEvent implements Serializable {

    public static final String EVENT_TYPE = "CART_EXPIRED";

    private String cartId;
    private String customerId;
    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp;

    public CartExpiredEvent() {
    }

    public CartExpiredEvent(String cartId, String customerId, LocalDateTime timestamp) {
        this.cartId = cartId;
        this.customerId = customerId;
        this.timestamp = timestamp;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
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
