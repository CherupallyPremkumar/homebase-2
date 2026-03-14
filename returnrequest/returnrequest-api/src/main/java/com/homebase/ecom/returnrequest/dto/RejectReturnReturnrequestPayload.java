package com.homebase.ecom.returnrequest.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the rejectReturn event.
 * Carries rejection reason and comment.
 */
public class RejectReturnReturnrequestPayload extends MinimalPayload {

    private String rejectionReason;

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}
