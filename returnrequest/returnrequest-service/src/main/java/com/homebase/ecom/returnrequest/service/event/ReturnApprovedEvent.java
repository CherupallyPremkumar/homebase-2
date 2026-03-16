package com.homebase.ecom.returnrequest.service.event;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Event published when a return request is approved.
 * Published to return.events topic.
 */
public class ReturnApprovedEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String EVENT_TYPE = "RETURN_APPROVED";

    private String returnRequestId;
    private String orderId;
    private String customerId;
    private BigDecimal totalRefundAmount;
    private String returnType;
    private LocalDateTime approvedAt;

    public ReturnApprovedEvent() {}

    public ReturnApprovedEvent(String returnRequestId, String orderId, String customerId,
                               BigDecimal totalRefundAmount, String returnType) {
        this.returnRequestId = returnRequestId;
        this.orderId = orderId;
        this.customerId = customerId;
        this.totalRefundAmount = totalRefundAmount;
        this.returnType = returnType;
        this.approvedAt = LocalDateTime.now();
    }

    public String getReturnRequestId() { return returnRequestId; }
    public void setReturnRequestId(String returnRequestId) { this.returnRequestId = returnRequestId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public BigDecimal getTotalRefundAmount() { return totalRefundAmount; }
    public void setTotalRefundAmount(BigDecimal totalRefundAmount) { this.totalRefundAmount = totalRefundAmount; }

    public String getReturnType() { return returnType; }
    public void setReturnType(String returnType) { this.returnType = returnType; }

    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
}
