package com.homebase.ecom.notification.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the fail event (SENDING -> FAILED).
 * Records the error details and increments the retry count.
 */
public class FailNotificationPayload extends MinimalPayload {
    private String failureReason;

    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
}
