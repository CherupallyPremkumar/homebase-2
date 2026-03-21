package com.homebase.ecom.notification.domain.port;

import com.homebase.ecom.notification.domain.model.Notification;

/**
 * Outbound port for publishing notification domain events.
 * Infrastructure layer provides the Kafka-backed implementation.
 * No @Component -- wired explicitly via @Bean.
 */
public interface NotificationEventPublisherPort {

    /**
     * Publish event when notification is successfully sent.
     *
     * @param notification the sent notification
     */
    void publishNotificationSent(Notification notification);

    /**
     * Publish event when notification bounces (permanent failure, retries exhausted).
     *
     * @param notification the bounced notification
     */
    void publishNotificationBounced(Notification notification);

    /**
     * Publish event when notification delivery fails (may be retried).
     *
     * @param notification the failed notification
     */
    void publishNotificationFailed(Notification notification);
}
