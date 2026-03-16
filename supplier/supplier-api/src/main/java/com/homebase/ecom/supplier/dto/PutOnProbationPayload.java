package com.homebase.ecom.supplier.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the putOnProbation event.
 * Admin or system places a supplier on probation for performance review.
 */
public class PutOnProbationPayload extends MinimalPayload {

    private String reason;
    private int reviewPeriodDays;

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public int getReviewPeriodDays() { return reviewPeriodDays; }
    public void setReviewPeriodDays(int reviewPeriodDays) { this.reviewPeriodDays = reviewPeriodDays; }
}
