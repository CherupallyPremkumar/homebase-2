package com.homebase.ecom.review.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for flagging a review for re-moderation.
 */
public class FlagReviewPayload extends MinimalPayload {
    private String flagReason;

    public String getFlagReason() { return flagReason; }
    public void setFlagReason(String flagReason) { this.flagReason = flagReason; }
}
