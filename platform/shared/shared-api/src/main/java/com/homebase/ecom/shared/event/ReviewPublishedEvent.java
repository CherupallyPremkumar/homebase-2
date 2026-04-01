package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Event published when a review is published (approved/auto-published).
 * Consumers: Product BC (recalculate average rating).
 */
public class ReviewPublishedEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String EVENT_TYPE = "REVIEW_PUBLISHED";

    private String reviewId;
    private String productId;
    private String customerId;
    private int rating;
    private boolean verifiedPurchase;
    private LocalDateTime publishedAt;

    public ReviewPublishedEvent() {}

    public ReviewPublishedEvent(String reviewId, String productId, String customerId,
                                int rating, boolean verifiedPurchase) {
        this.reviewId = reviewId;
        this.productId = productId;
        this.customerId = customerId;
        this.rating = rating;
        this.verifiedPurchase = verifiedPurchase;
        this.publishedAt = LocalDateTime.now();
    }

    public String getEventType() { return EVENT_TYPE; }

    public String getReviewId() { return reviewId; }
    public void setReviewId(String reviewId) { this.reviewId = reviewId; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public boolean isVerifiedPurchase() { return verifiedPurchase; }
    public void setVerifiedPurchase(boolean verifiedPurchase) { this.verifiedPurchase = verifiedPurchase; }

    public LocalDateTime getPublishedAt() { return publishedAt; }
    public void setPublishedAt(LocalDateTime publishedAt) { this.publishedAt = publishedAt; }
}
