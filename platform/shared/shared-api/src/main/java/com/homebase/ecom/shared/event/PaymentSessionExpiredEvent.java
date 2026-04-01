package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Kafka event DTO published when a Stripe checkout session expires.
 */
public class PaymentSessionExpiredEvent implements Serializable {

    public static final String EVENT_TYPE = "PAYMENT_SESSION_EXPIRED";

    private String orderId;
    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp;

    public PaymentSessionExpiredEvent() {
    }

    public PaymentSessionExpiredEvent(String orderId, LocalDateTime timestamp) {
        this.orderId = orderId;
        this.timestamp = timestamp;
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
