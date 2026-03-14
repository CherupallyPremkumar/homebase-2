package com.homebase.ecom.product.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the rejectQuality event.
 * Includes the measured quality score and a mandatory audit comment.
 */
public class RejectQualityProductPayload extends MinimalPayload {

    /**
     * Quality score observed by Hub Dad during physical inspection (0–100).
     * Used by rules.quality.autoRejectBelowScore.
     */
    private Integer qualityScore;

    /**
     * Reason for quality rejection. Required when
     * policies.lifecycle.rejectionRequiresComment = true.
     * Examples: "Loose threads detected", "Colour fade", "Rough texture".
     */
    private String comment;

    public Integer getQualityScore() {
        return qualityScore;
    }

    public void setQualityScore(Integer qualityScore) {
        this.qualityScore = qualityScore;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
