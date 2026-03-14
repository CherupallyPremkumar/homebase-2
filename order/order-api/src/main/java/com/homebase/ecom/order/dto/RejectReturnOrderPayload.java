package com.homebase.ecom.order.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Customized Payload for the rejectReturn event.
 */
public class RejectReturnOrderPayload extends MinimalPayload {
    private String reason;
    private String rejectedBy;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRejectedBy() {
        return rejectedBy;
    }

    public void setRejectedBy(String rejectedBy) {
        this.rejectedBy = rejectedBy;
    }
}
