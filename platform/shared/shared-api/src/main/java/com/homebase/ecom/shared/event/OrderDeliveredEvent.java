package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Event published when an order is delivered to the customer.
 */
public class OrderDeliveredEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderId;
    private String customerId;
    private LocalDateTime deliveredAt;

    public OrderDeliveredEvent() {
    }

    public OrderDeliveredEvent(String orderId, String customerId, LocalDateTime deliveredAt) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.deliveredAt = deliveredAt;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public LocalDateTime getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; }
}
