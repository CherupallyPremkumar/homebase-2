package com.homebase.ecom.review.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for archiving a review (PUBLISHED -> ARCHIVED).
 */
public class ArchiveReviewPayload extends MinimalPayload {
    private String archiveReason;

    public String getArchiveReason() { return archiveReason; }
    public void setArchiveReason(String archiveReason) { this.archiveReason = archiveReason; }
}
