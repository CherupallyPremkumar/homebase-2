package com.homebase.ecom.order.model;

import com.homebase.ecom.shared.Money;

public class OrderItem {

    private String id;
    private String productId;
    private String supplierId;
    private String productName;
    private Integer quantity;
    private Money unitPrice;
    private Money totalPrice;
    private OrderItemStatus status = OrderItemStatus.PLACED;
    private String settlementId;
    private String settlementStatus = "PENDING";

    // --- Business Logic ---

    public boolean canCancel() {
        return this.status == OrderItemStatus.PLACED;
    }

    public boolean canRefund() {
        return this.status != OrderItemStatus.REFUNDED && this.status != OrderItemStatus.REFUND_REQUESTED;
    }

    public void requestCancellation() {
        if (!canCancel()) {
            throw new IllegalStateException("Item cannot be cancelled in state: " + this.status);
        }
        this.status = OrderItemStatus.CANCELLATION_REQUESTED;
    }

    public void cancel() { this.status = OrderItemStatus.CANCELLED; }
    public void requestRefund() { this.status = OrderItemStatus.REFUND_REQUESTED; }
    public void refund() { this.status = OrderItemStatus.REFUNDED; }

    // --- Getters & Setters ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public Money getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Money unitPrice) { this.unitPrice = unitPrice; }

    public Money getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Money totalPrice) { this.totalPrice = totalPrice; }

    public OrderItemStatus getStatus() { return status; }
    public void setStatus(OrderItemStatus status) { this.status = status; }

    public String getSettlementId() { return settlementId; }
    public void setSettlementId(String settlementId) {
        this.settlementId = settlementId;
        if (settlementId != null) {
            this.settlementStatus = "SETTLED";
        }
    }

    public String getSettlementStatus() { return settlementStatus; }
    public void setSettlementStatus(String settlementStatus) { this.settlementStatus = settlementStatus; }
}
