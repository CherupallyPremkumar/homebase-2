package com.homebase.ecom.returnrequest.service.event;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Event published when a refund has been processed for a return request.
 */
public class RefundProcessedEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String returnRequestId;
    private String orderId;
    private BigDecimal refundAmount;
    private String refundMethod;
    private String refundTransactionId;
    private LocalDateTime processedAt;

    public RefundProcessedEvent() {}

    public RefundProcessedEvent(String returnRequestId, String orderId, BigDecimal refundAmount,
                                String refundMethod, String refundTransactionId) {
        this.returnRequestId = returnRequestId;
        this.orderId = orderId;
        this.refundAmount = refundAmount;
        this.refundMethod = refundMethod;
        this.refundTransactionId = refundTransactionId;
        this.processedAt = LocalDateTime.now();
    }

    public String getReturnRequestId() { return returnRequestId; }
    public void setReturnRequestId(String returnRequestId) { this.returnRequestId = returnRequestId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public BigDecimal getRefundAmount() { return refundAmount; }
    public void setRefundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; }

    public String getRefundMethod() { return refundMethod; }
    public void setRefundMethod(String refundMethod) { this.refundMethod = refundMethod; }

    public String getRefundTransactionId() { return refundTransactionId; }
    public void setRefundTransactionId(String refundTransactionId) { this.refundTransactionId = refundTransactionId; }

    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }
}
