package com.homebase.ecom.shared.model.event;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Kafka event DTO published when a new order is created.
 */
public class OrderCreatedEvent implements Serializable {

    public static final String EVENT_TYPE = "ORDER_CREATED";

    private String orderId;
    private String customerId;
    private BigDecimal totalAmount;
    private List<OrderItemPayload> items;
    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp;

    public OrderCreatedEvent() {
    }

    public OrderCreatedEvent(String orderId, String customerId, BigDecimal totalAmount,
            List<OrderItemPayload> items, LocalDateTime timestamp) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.items = items;
        this.timestamp = timestamp;
    }

    // --- Getters & Setters ---

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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<OrderItemPayload> getItems() {
        return items;
    }

    public void setItems(List<OrderItemPayload> items) {
        this.items = items;
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

    /**
     * Lightweight item payload for Kafka event.
     */
    public static class OrderItemPayload implements Serializable {
        private String productId;
        private Integer quantity;

        public OrderItemPayload() {
        }

        public OrderItemPayload(String productId, Integer quantity) {
            this.productId = productId;
            this.quantity = quantity;
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
    }
}
