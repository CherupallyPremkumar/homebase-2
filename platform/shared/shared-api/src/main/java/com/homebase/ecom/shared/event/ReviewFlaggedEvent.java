package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Event published when a review is flagged for moderation.
 * Consumers: Notification BC (alert moderators).
 */
public class ReviewFlaggedEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String EVENT_TYPE = "REVIEW_FLAGGED";

    private String reviewId;
    private String productId;
    private String customerId;
    private String flagReason;
    private String previousState;
    private LocalDateTime flaggedAt;

    public ReviewFlaggedEvent() {}

    public ReviewFlaggedEvent(String reviewId, String productId, String customerId,
                              String flagReason, String previousState) {
        this.reviewId = reviewId;
        this.productId = productId;
        this.customerId = customerId;
        this.flagReason = flagReason;
        this.previousState = previousState;
        this.flaggedAt = LocalDateTime.now();
    }

    public String getEventType() { return EVENT_TYPE; }

    public String getReviewId() { return reviewId; }
    public void setReviewId(String reviewId) { this.reviewId = reviewId; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getFlagReason() { return flagReason; }
    public void setFlagReason(String flagReason) { this.flagReason = flagReason; }

    public String getPreviousState() { return previousState; }
    public void setPreviousState(String previousState) { this.previousState = previousState; }

    public LocalDateTime getFlaggedAt() { return flaggedAt; }
    public void setFlaggedAt(LocalDateTime flaggedAt) { this.flaggedAt = flaggedAt; }
}
