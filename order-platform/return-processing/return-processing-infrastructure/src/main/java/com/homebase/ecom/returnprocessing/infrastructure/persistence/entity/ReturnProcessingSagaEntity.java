package com.homebase.ecom.returnprocessing.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "return_processing_saga")
public class ReturnProcessingSagaEntity extends AbstractJpaStateEntity {

    @Column(name = "return_request_id")
    private String returnRequestId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "order_item_id")
    private String orderItemId;

    @Column(name = "refund_amount")
    private BigDecimal refundAmount;

    @Column(name = "shipment_id")
    private String shipmentId;

    @Column(name = "settlement_adjustment_id")
    private String settlementAdjustmentId;

    @Column(name = "refund_id")
    private String refundId;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "retry_count")
    private int retryCount;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "saga_id")
    private List<ReturnProcessingSagaActivityLogEntity> activities = new ArrayList<>();

    public String getReturnRequestId() {
        return returnRequestId;
    }

    public void setReturnRequestId(String returnRequestId) {
        this.returnRequestId = returnRequestId;
    }

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

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getSettlementAdjustmentId() {
        return settlementAdjustmentId;
    }

    public void setSettlementAdjustmentId(String settlementAdjustmentId) {
        this.settlementAdjustmentId = settlementAdjustmentId;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public List<ReturnProcessingSagaActivityLogEntity> getActivities() {
        return activities;
    }

    public void setActivities(List<ReturnProcessingSagaActivityLogEntity> activities) {
        this.activities = activities;
    }
}
