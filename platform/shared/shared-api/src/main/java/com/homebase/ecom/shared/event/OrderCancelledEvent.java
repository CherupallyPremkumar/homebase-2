package com.homebase.ecom.shared.event;

import java.time.LocalDateTime;
import java.util.List;

public class OrderCancelledEvent {
    private String orderId;
    private String customerId;
    private List<OrderItemPayload> items;
    private LocalDateTime timestamp;

    public OrderCancelledEvent() {
    }

    public OrderCancelledEvent(String orderId, String customerId, List<OrderItemPayload> items,
            LocalDateTime timestamp) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = items;
        this.timestamp = timestamp;
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

    public List<OrderItemPayload> getItems() {
        return items;
    }

    public void setItems(List<OrderItemPayload> items) {
        this.items = items;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public static class OrderItemPayload {
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
