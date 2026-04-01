package com.homebase.ecom.review.domain.port;

import com.homebase.ecom.review.model.Review;

/**
 * Outbound port for publishing review domain events.
 * Infrastructure layer provides Kafka implementation.
 * Domain/service layer depends only on this interface -- never on ChenilePub directly.
 */
public interface ReviewEventPublisherPort {

    /**
     * Publishes event when a review is submitted for moderation.
     */
    void publishReviewSubmitted(Review review);

    /**
     * Publishes event when a review is published (visible to customers).
     * Product BC consumes this to update average rating aggregates.
     */
    void publishReviewPublished(Review review);

    /**
     * Publishes event when a review is flagged for moderation.
     * Notification BC consumes this to alert moderators.
     */
    void publishReviewFlagged(Review review, String previousState);
}
