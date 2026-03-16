package com.homebase.ecom.supplier.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the reviewSupplier event (APPLIED -> UNDER_REVIEW).
 * Admin picks up a supplier application for review.
 */
public class ReviewSupplierPayload extends MinimalPayload {

    private String reviewNotes;

    public String getReviewNotes() { return reviewNotes; }
    public void setReviewNotes(String reviewNotes) { this.reviewNotes = reviewNotes; }
}
