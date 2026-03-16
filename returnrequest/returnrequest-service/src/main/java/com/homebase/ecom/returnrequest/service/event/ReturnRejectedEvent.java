package com.homebase.ecom.returnrequest.service.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Event published when a return request is rejected.
 * Published to return.events topic.
 */
public class ReturnRejectedEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String EVENT_TYPE = "RETURN_REJECTED";

    private String returnRequestId;
    private String orderId;
    private String customerId;
    private String rejectionReason;
    private String rejectionComment;
    private LocalDateTime rejectedAt;

    public ReturnRejectedEvent() {}

    public ReturnRejectedEvent(String returnRequestId, String orderId, String customerId,
                               String rejectionReason, String rejectionComment) {
        this.returnRequestId = returnRequestId;
        this.orderId = orderId;
        this.customerId = customerId;
        this.rejectionReason = rejectionReason;
        this.rejectionComment = rejectionComment;
        this.rejectedAt = LocalDateTime.now();
    }

    public String getReturnRequestId() { return returnRequestId; }
    public void setReturnRequestId(String returnRequestId) { this.returnRequestId = returnRequestId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public String getRejectionComment() { return rejectionComment; }
    public void setRejectionComment(String rejectionComment) { this.rejectionComment = rejectionComment; }

    public LocalDateTime getRejectedAt() { return rejectedAt; }
    public void setRejectedAt(LocalDateTime rejectedAt) { this.rejectedAt = rejectedAt; }
}
