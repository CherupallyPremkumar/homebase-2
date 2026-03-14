package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Event published when a specific item in an order is requested to be
 * cancelled.
 */
public class OrderItemCancellationRequestedEvent implements Serializable {
    private String orderId;
    private String orderItemId;
    private String productId;
    private Integer quantity;
    private String reason;
    private LocalDateTime timestamp;

    public OrderItemCancellationRequestedEvent() {
    }

    public OrderItemCancellationRequestedEvent(String orderId, String orderItemId, String productId, Integer quantity,
            String reason) {
        this.orderId = orderId;
        this.orderItemId = orderItemId;
        this.productId = productId;
        this.quantity = quantity;
        this.reason = reason;
        this.timestamp = LocalDateTime.now();
    }

    // --- Getters & Setters ---

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
