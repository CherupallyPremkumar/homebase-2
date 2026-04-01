package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Event published when a customer initiates a return.
 */
public class ReturnInitiatedEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderId;
    private String customerId;
    private String reason;
    private LocalDateTime initiatedAt;
    private List<ReturnedItem> returnedItems;

    public ReturnInitiatedEvent() {
    }

    public ReturnInitiatedEvent(String orderId, String customerId, String reason,
            LocalDateTime initiatedAt, List<ReturnedItem> returnedItems) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.reason = reason;
        this.initiatedAt = initiatedAt;
        this.returnedItems = returnedItems;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public LocalDateTime getInitiatedAt() { return initiatedAt; }
    public void setInitiatedAt(LocalDateTime initiatedAt) { this.initiatedAt = initiatedAt; }

    public List<ReturnedItem> getReturnedItems() { return returnedItems; }
    public void setReturnedItems(List<ReturnedItem> returnedItems) { this.returnedItems = returnedItems; }

    public static class ReturnedItem implements Serializable {
        private String productId;
        private Integer quantity;

        public ReturnedItem() {
        }

        public ReturnedItem(String productId, Integer quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }
}
