package com.homebase.ecom.checkout.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Base class for all checkout domain events.
 * Each subclass represents a specific lifecycle transition.
 * Adapters serialize this to JSON and publish to "checkout.events" topic.
 */
public abstract class CheckoutEvent implements Serializable {

    private final String eventType;
    private final String checkoutId;
    private final LocalDateTime timestamp;

    protected CheckoutEvent(String eventType, String checkoutId, LocalDateTime timestamp) {
        this.eventType = eventType;
        this.checkoutId = checkoutId;
        this.timestamp = timestamp;
    }

    public String getEventType() {
        return eventType;
    }

    public String getCheckoutId() {
        return checkoutId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
