package com.homebase.ecom.review.domain.port;

/**
 * Outbound port for sending notifications related to review events.
 * Abstracts the underlying notification mechanism (email, push, in-app, Kafka event).
 */
public interface NotificationPort {

    /**
     * Notify moderators that a review needs attention.
     *
     * @param reviewId The review ID
     * @param productId The product ID
     * @param reason The reason for flagging/moderation
     */
    void notifyModerators(String reviewId, String productId, String reason);

    /**
     * Notify the customer about their review status change.
     *
     * @param customerId The customer ID
     * @param reviewId The review ID
     * @param newStatus The new review status
     * @param message Optional message (e.g., rejection reason, edit instructions)
     */
    void notifyCustomer(String customerId, String reviewId, String newStatus, String message);
}
