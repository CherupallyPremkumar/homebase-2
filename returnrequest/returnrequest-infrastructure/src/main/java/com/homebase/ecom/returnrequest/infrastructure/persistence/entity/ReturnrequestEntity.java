package com.homebase.ecom.returnrequest.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.chenile.jpautils.entity.AbstractJpaStateEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "returnrequest")
public class ReturnrequestEntity extends AbstractJpaStateEntity {

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "order_item_id", nullable = false)
    private String orderItemId;

    @Column(name = "reason", length = 1000)
    private String reason;

    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;

    @Column(name = "refund_amount")
    private BigDecimal refundAmount;

    @Column(name = "return_type", length = 50)
    private String returnType;

    @Column(name = "description")
    private String description;

    @Column(name = "item_price")
    private BigDecimal itemPrice;

    @Column(name = "order_delivery_date")
    private java.time.LocalDateTime orderDeliveryDate;

    @Column(name = "inspector_id")
    private String inspectorId;

    @Column(name = "inspector_notes", length = 2000)
    private String inspectorNotes;

    @Column(name = "rejection_reason", length = 1000)
    private String rejectionReason;

    @Column(name = "rejection_comment", length = 2000)
    private String rejectionComment;

    @Column(name = "pickup_tracking_number")
    private String pickupTrackingNumber;

    @Column(name = "warehouse_id")
    private String warehouseId;

    @Column(name = "condition_on_receipt")
    private String conditionOnReceipt;

    @Column(name = "refund_method")
    private String refundMethod;

    @Column(name = "refund_transaction_id")
    private String refundTransactionId;

    @Column(name = "refund_processed_at")
    private java.time.LocalDateTime refundProcessedAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "returnrequest_id")
    private List<ReturnrequestActivityLogEntity> activities = new ArrayList<>();

    // Getters and Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getOrderItemId() { return orderItemId; }
    public void setOrderItemId(String orderItemId) { this.orderItemId = orderItemId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getRefundAmount() { return refundAmount; }
    public void setRefundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; }

    public String getReturnType() { return returnType; }
    public void setReturnType(String returnType) { this.returnType = returnType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<ReturnrequestActivityLogEntity> getActivities() { return activities; }
    public void setActivities(List<ReturnrequestActivityLogEntity> activities) { this.activities = activities; }

    public BigDecimal getItemPrice() { return itemPrice; }
    public void setItemPrice(BigDecimal itemPrice) { this.itemPrice = itemPrice; }

    public java.time.LocalDateTime getOrderDeliveryDate() { return orderDeliveryDate; }
    public void setOrderDeliveryDate(java.time.LocalDateTime orderDeliveryDate) { this.orderDeliveryDate = orderDeliveryDate; }

    public String getInspectorId() { return inspectorId; }
    public void setInspectorId(String inspectorId) { this.inspectorId = inspectorId; }

    public String getInspectorNotes() { return inspectorNotes; }
    public void setInspectorNotes(String inspectorNotes) { this.inspectorNotes = inspectorNotes; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public String getRejectionComment() { return rejectionComment; }
    public void setRejectionComment(String rejectionComment) { this.rejectionComment = rejectionComment; }

    public String getPickupTrackingNumber() { return pickupTrackingNumber; }
    public void setPickupTrackingNumber(String pickupTrackingNumber) { this.pickupTrackingNumber = pickupTrackingNumber; }

    public String getWarehouseId() { return warehouseId; }
    public void setWarehouseId(String warehouseId) { this.warehouseId = warehouseId; }

    public String getConditionOnReceipt() { return conditionOnReceipt; }
    public void setConditionOnReceipt(String conditionOnReceipt) { this.conditionOnReceipt = conditionOnReceipt; }

    public String getRefundMethod() { return refundMethod; }
    public void setRefundMethod(String refundMethod) { this.refundMethod = refundMethod; }

    public String getRefundTransactionId() { return refundTransactionId; }
    public void setRefundTransactionId(String refundTransactionId) { this.refundTransactionId = refundTransactionId; }

    public java.time.LocalDateTime getRefundProcessedAt() { return refundProcessedAt; }
    public void setRefundProcessedAt(java.time.LocalDateTime refundProcessedAt) { this.refundProcessedAt = refundProcessedAt; }
}
