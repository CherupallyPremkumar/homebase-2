package com.homebase.ecom.payment.dto;

/**
 * Payload for recording authentication failure.
 */
public class FailAuthenticationPayload {
    private String comment;
    private String failureReason;

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
}
