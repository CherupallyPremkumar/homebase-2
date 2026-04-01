package com.homebase.ecom.returnrequest.service.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Event published when a return request is created.
 * Published to return.events topic.
 */
public class ReturnRequestedEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String EVENT_TYPE = "RETURN_REQUESTED";

    private String returnRequestId;
    private String orderId;
    private String customerId;
    private String reason;
    private String returnType;
    private LocalDateTime requestedAt;

    public ReturnRequestedEvent() {}

    public ReturnRequestedEvent(String returnRequestId, String orderId, String customerId,
                                String reason, String returnType) {
        this.returnRequestId = returnRequestId;
        this.orderId = orderId;
        this.customerId = customerId;
        this.reason = reason;
        this.returnType = returnType;
        this.requestedAt = LocalDateTime.now();
    }

    public String getReturnRequestId() { return returnRequestId; }
    public void setReturnRequestId(String returnRequestId) { this.returnRequestId = returnRequestId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getReturnType() { return returnType; }
    public void setReturnType(String returnType) { this.returnType = returnType; }

    public LocalDateTime getRequestedAt() { return requestedAt; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }
}
