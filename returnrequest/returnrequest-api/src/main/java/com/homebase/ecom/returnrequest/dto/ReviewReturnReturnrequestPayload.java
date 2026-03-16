package com.homebase.ecom.returnrequest.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the reviewReturn event (REQUESTED -> CHECK_AUTO_APPROVE).
 * Support/Admin reviews the return request and triggers auto-approve check.
 */
public class ReviewReturnReturnrequestPayload extends MinimalPayload {

    private String reviewerId;
    private String reviewNotes;

    public String getReviewerId() { return reviewerId; }
    public void setReviewerId(String reviewerId) { this.reviewerId = reviewerId; }

    public String getReviewNotes() { return reviewNotes; }
    public void setReviewNotes(String reviewNotes) { this.reviewNotes = reviewNotes; }
}
