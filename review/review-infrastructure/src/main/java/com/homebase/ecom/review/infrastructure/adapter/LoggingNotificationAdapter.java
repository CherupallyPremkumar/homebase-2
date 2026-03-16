package com.homebase.ecom.review.infrastructure.adapter;

import com.homebase.ecom.review.domain.port.NotificationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default notification adapter that logs notifications.
 * In production, replace with an adapter that publishes to the Notification BC via Kafka.
 */
public class LoggingNotificationAdapter implements NotificationPort {

    private static final Logger log = LoggerFactory.getLogger(LoggingNotificationAdapter.class);

    @Override
    public void notifyModerators(String reviewId, String productId, String reason) {
        log.info("NOTIFICATION: Moderators alerted - Review {} for product {} needs attention. Reason: {}",
                reviewId, productId, reason);
    }

    @Override
    public void notifyCustomer(String customerId, String reviewId, String newStatus, String message) {
        log.info("NOTIFICATION: Customer {} notified - Review {} status changed to {}. Message: {}",
                customerId, reviewId, newStatus, message);
    }
}
