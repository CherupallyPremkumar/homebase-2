package com.homebase.ecom.review.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for sending a flagged review back to moderation (FLAGGED -> UNDER_MODERATION).
 */
public class ModerateReviewPayload extends MinimalPayload {
    private String moderatorNotes;

    public String getModeratorNotes() { return moderatorNotes; }
    public void setModeratorNotes(String moderatorNotes) { this.moderatorNotes = moderatorNotes; }
}
