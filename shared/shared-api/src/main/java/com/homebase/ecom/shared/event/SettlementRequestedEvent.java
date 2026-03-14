package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Event published when a completed order triggers settlement processing.
 * Consumed by: settlement BC to initiate supplier payouts.
 */
public class SettlementRequestedEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderId;
    private LocalDateTime completedAt;
    private List<SettlementLineItem> items;

    public SettlementRequestedEvent() {
    }

    public SettlementRequestedEvent(String orderId, LocalDateTime completedAt,
            List<SettlementLineItem> items) {
        this.orderId = orderId;
        this.completedAt = completedAt;
        this.items = items;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public List<SettlementLineItem> getItems() { return items; }
    public void setItems(List<SettlementLineItem> items) { this.items = items; }

    public static class SettlementLineItem implements Serializable {
        private String productId;
        private String supplierId;
        private Integer quantity;
        private BigDecimal amount;

        public SettlementLineItem() {
        }

        public SettlementLineItem(String productId, String supplierId, Integer quantity, BigDecimal amount) {
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
