package com.homebase.ecom.returnrequest.service.event;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Event published when a return request is approved.
 */
public class ReturnApprovedEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String returnRequestId;
    private String orderId;
    private String orderItemId;
    private BigDecimal refundAmount;
    private String returnType;
    private LocalDateTime approvedAt;

    public ReturnApprovedEvent() {}

    public ReturnApprovedEvent(String returnRequestId, String orderId, String orderItemId,
                               BigDecimal refundAmount, String returnType) {
        this.returnRequestId = returnRequestId;
        this.orderId = orderId;
        this.orderItemId = orderItemId;
        this.refundAmount = refundAmount;
        this.returnType = returnType;
        this.approvedAt = LocalDateTime.now();
    }

    public String getReturnRequestId() { return returnRequestId; }
    public void setReturnRequestId(String returnRequestId) { this.returnRequestId = returnRequestId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getOrderItemId() { return orderItemId; }
    public void setOrderItemId(String orderItemId) { this.orderItemId = orderItemId; }

    public BigDecimal getRefundAmount() { return refundAmount; }
    public void setRefundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; }

    public String getReturnType() { return returnType; }
    public void setReturnType(String returnType) { this.returnType = returnType; }

    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
}
