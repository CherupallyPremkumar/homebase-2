package com.homebase.ecom.review.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for publishing a review (UNDER_MODERATION/FLAGGED -> PUBLISHED).
 */
public class PublishReviewPayload extends MinimalPayload {
    private String moderatorNotes;

    public String getModeratorNotes() { return moderatorNotes; }
    public void setModeratorNotes(String moderatorNotes) { this.moderatorNotes = moderatorNotes; }
}
