package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Event published when a customer confirms delivery and order is completed.
 * Consumed by: settlement BC to trigger supplier payouts.
 */
public class OrderCompletedEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderId;
    private String customerId;
    private LocalDateTime completedAt;
    private List<CompletedItem> items;

    public OrderCompletedEvent() {
    }

    public OrderCompletedEvent(String orderId, String customerId, LocalDateTime completedAt,
            List<CompletedItem> items) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.completedAt = completedAt;
        this.items = items;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public List<CompletedItem> getItems() { return items; }
    public void setItems(List<CompletedItem> items) { this.items = items; }

    public static class CompletedItem implements Serializable {
        private String productId;
        private String supplierId;
        private Integer quantity;
        private BigDecimal amount;

        public CompletedItem() {
        }

        public CompletedItem(String productId, String supplierId, Integer quantity, BigDecimal amount) {
            this.productId = productId;
            this.supplierId = supplierId;
            this.quantity = quantity;
            this.amount = amount;
        }

        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }

        public String getSupplierId() { return supplierId; }
        public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
    }
}
