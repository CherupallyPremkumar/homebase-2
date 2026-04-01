package com.homebase.ecom.order.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for clearFraud event -- admin clears fraud hold after review.
 */
public class ClearFraudOrderPayload extends MinimalPayload {
    private String reviewedBy;
    private String clearanceNote;

    public String getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(String reviewedBy) { this.reviewedBy = reviewedBy; }

    public String getClearanceNote() { return clearanceNote; }
    public void setClearanceNote(String clearanceNote) { this.clearanceNote = clearanceNote; }
}
