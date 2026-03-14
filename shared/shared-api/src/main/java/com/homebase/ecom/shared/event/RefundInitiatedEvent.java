package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Event published when a return is approved and refund processing begins.
 * Consumed by: payment BC to process the refund.
 */
public class RefundInitiatedEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderId;
    private String customerId;
    private BigDecimal refundAmount;
    private String currency;
    private LocalDateTime initiatedAt;
    private List<RefundItem> items;

    public RefundInitiatedEvent() {
    }

    public RefundInitiatedEvent(String orderId, String customerId, BigDecimal refundAmount,
            String currency, LocalDateTime initiatedAt, List<RefundItem> items) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.refundAmount = refundAmount;
        this.currency = currency;
        this.initiatedAt = initiatedAt;
        this.items = items;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public BigDecimal getRefundAmount() { return refundAmount; }
    public void setRefundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public LocalDateTime getInitiatedAt() { return initiatedAt; }
    public void setInitiatedAt(LocalDateTime initiatedAt) { this.initiatedAt = initiatedAt; }

    public List<RefundItem> getItems() { return items; }
    public void setItems(List<RefundItem> items) { this.items = items; }

    public static class RefundItem implements Serializable {
        private String productId;
        private Integer quantity;
        private BigDecimal amount;

        public RefundItem() {
        }

        public RefundItem(String productId, Integer quantity, BigDecimal amount) {
            this.productId = productId;
            this.quantity = quantity;
            this.amount = amount;
        }

        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
    }
}
