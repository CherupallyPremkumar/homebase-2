package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Event published when a review is submitted.
 */
public class ReviewSubmittedEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String EVENT_TYPE = "REVIEW_SUBMITTED";

    private String reviewId;
    private String productId;
    private String customerId;
    private String orderId;
    private int rating;
    private boolean verifiedPurchase;
    private LocalDateTime submittedAt;

    public ReviewSubmittedEvent() {}

    public ReviewSubmittedEvent(String reviewId, String productId, String customerId,
                                String orderId, int rating, boolean verifiedPurchase) {
        this.reviewId = reviewId;
        this.productId = productId;
        this.customerId = customerId;
        this.orderId = orderId;
        this.rating = rating;
        this.verifiedPurchase = verifiedPurchase;
        this.submittedAt = LocalDateTime.now();
    }

    public String getEventType() { return EVENT_TYPE; }

    public String getReviewId() { return reviewId; }
    public void setReviewId(String reviewId) { this.reviewId = reviewId; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public boolean isVerifiedPurchase() { return verifiedPurchase; }
    public void setVerifiedPurchase(boolean verifiedPurchase) { this.verifiedPurchase = verifiedPurchase; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
}
