package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Event published by Checkout service when checkout saga completes.
 * Contains cartId and orderId so cart can be closed and order tracked.
 */
public class CheckoutCompletedEvent implements Serializable {

    public static final String EVENT_TYPE = "CHECKOUT_COMPLETED";

    private String cartId;
    private String orderId;
    private String customerId;
    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp = LocalDateTime.now();

    public CheckoutCompletedEvent() {}

    public CheckoutCompletedEvent(String cartId, String orderId, String customerId) {
        this.cartId = cartId;
        this.orderId = orderId;
        this.customerId = customerId;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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
