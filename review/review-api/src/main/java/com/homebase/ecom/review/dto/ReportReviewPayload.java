package com.homebase.ecom.review.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for reporting a review (self-transition on PUBLISHED, increments reportCount).
 */
public class ReportReviewPayload extends MinimalPayload {
    private String reportReason;

    public String getReportReason() { return reportReason; }
    public void setReportReason(String reportReason) { this.reportReason = reportReason; }
}
