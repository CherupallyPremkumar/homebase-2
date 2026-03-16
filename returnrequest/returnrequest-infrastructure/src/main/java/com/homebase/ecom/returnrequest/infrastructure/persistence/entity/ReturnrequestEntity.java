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

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "items_json", length = 4000)
    private String itemsJson;

    @Column(name = "reason", length = 1000)
    private String reason;

    @Column(name = "return_type", length = 50)
    private String returnType;

    @Column(name = "total_refund_amount")
    private BigDecimal totalRefundAmount;

    @Column(name = "restocking_fee")
    private BigDecimal restockingFee;

    @Column(name = "description")
    private String description;

    @Column(name = "reviewer_id")
    private String reviewerId;

    @Column(name = "review_notes", length = 2000)
    private String reviewNotes;

    @Column(name = "rejection_reason", length = 1000)
    private String rejectionReason;

    @Column(name = "rejection_comment", length = 2000)
    private String rejectionComment;

    @Column(name = "warehouse_id")
    private String warehouseId;

    @Column(name = "condition_on_receipt")
    private String conditionOnReceipt;

    @Column(name = "inspector_id")
    private String inspectorId;

    @Column(name = "inspector_notes", length = 2000)
    private String inspectorNotes;

    @Column(name = "order_delivery_date")
    private java.time.LocalDateTime orderDeliveryDate;

    @Column(name = "order_total_value")
    private BigDecimal orderTotalValue;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "returnrequest_id")
    private List<ReturnrequestActivityLogEntity> activities = new ArrayList<>();

    // Getters and Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getItemsJson() { return itemsJson; }
    public void setItemsJson(String itemsJson) { this.itemsJson = itemsJson; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getReturnType() { return returnType; }
    public void setReturnType(String returnType) { this.returnType = returnType; }

    public BigDecimal getTotalRefundAmount() { return totalRefundAmount; }
    public void setTotalRefundAmount(BigDecimal totalRefundAmount) { this.totalRefundAmount = totalRefundAmount; }

    public BigDecimal getRestockingFee() { return restockingFee; }
    public void setRestockingFee(BigDecimal restockingFee) { this.restockingFee = restockingFee; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getReviewerId() { return reviewerId; }
    public void setReviewerId(String reviewerId) { this.reviewerId = reviewerId; }

    public String getReviewNotes() { return reviewNotes; }
    public void setReviewNotes(String reviewNotes) { this.reviewNotes = reviewNotes; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public String getRejectionComment() { return rejectionComment; }
    public void setRejectionComment(String rejectionComment) { this.rejectionComment = rejectionComment; }

    public String getWarehouseId() { return warehouseId; }
    public void setWarehouseId(String warehouseId) { this.warehouseId = warehouseId; }

    public String getConditionOnReceipt() { return conditionOnReceipt; }
    public void setConditionOnReceipt(String conditionOnReceipt) { this.conditionOnReceipt = conditionOnReceipt; }

    public String getInspectorId() { return inspectorId; }
    public void setInspectorId(String inspectorId) { this.inspectorId = inspectorId; }

    public String getInspectorNotes() { return inspectorNotes; }
    public void setInspectorNotes(String inspectorNotes) { this.inspectorNotes = inspectorNotes; }

    public java.time.LocalDateTime getOrderDeliveryDate() { return orderDeliveryDate; }
    public void setOrderDeliveryDate(java.time.LocalDateTime orderDeliveryDate) { this.orderDeliveryDate = orderDeliveryDate; }

    public BigDecimal getOrderTotalValue() { return orderTotalValue; }
    public void setOrderTotalValue(BigDecimal orderTotalValue) { this.orderTotalValue = orderTotalValue; }

    public List<ReturnrequestActivityLogEntity> getActivities() { return activities; }
    public void setActivities(List<ReturnrequestActivityLogEntity> activities) { this.activities = activities; }
}
