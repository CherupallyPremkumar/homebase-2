package com.homebase.ecom.order.dto;

import org.chenile.workflow.param.MinimalPayload;

import java.math.BigDecimal;

/**
 * Payload for completeRefund event.
 */
public class CompleteRefundOrderPayload extends MinimalPayload {
    private String refundId;
    private BigDecimal refundAmount;

    public String getRefundId() { return refundId; }
    public void setRefundId(String refundId) { this.refundId = refundId; }

    public BigDecimal getRefundAmount() { return refundAmount; }
    public void setRefundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; }
}
