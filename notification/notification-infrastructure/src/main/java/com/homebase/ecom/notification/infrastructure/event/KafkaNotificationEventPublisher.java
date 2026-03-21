package com.homebase.ecom.notification.infrastructure.event;

import com.homebase.ecom.notification.domain.model.Notification;
import com.homebase.ecom.notification.domain.port.NotificationEventPublisherPort;
import com.homebase.ecom.shared.event.KafkaTopics;
import org.chenile.pubsub.ChenilePub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka-backed implementation of NotificationEventPublisherPort.
 * Translates domain method calls into Kafka messages via ChenilePub.
 * No @Component -- wired explicitly via @Bean.
 */
public class KafkaNotificationEventPublisher implements NotificationEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaNotificationEventPublisher.class);

    private final ChenilePub chenilePub;
    private final ObjectMapper objectMapper;

    public KafkaNotificationEventPublisher(ChenilePub chenilePub, ObjectMapper objectMapper) {
        this.chenilePub = chenilePub;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publishNotificationSent(Notification notification) {
        Map<String, Object> event = Map.of(
                "eventType", "NOTIFICATION_SENT",
                "notificationId", notification.getId(),
                "customerId", nullSafe(notification.getCustomerId()),
                "channel", nullSafe(notification.getChannel()),
                "templateId", nullSafe(notification.getTemplateId()));
        publish(event, "NOTIFICATION_SENT", notification.getId());
    }

    @Override
    public void publishNotificationBounced(Notification notification) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "NOTIFICATION_BOUNCED");
        event.put("notificationId", notification.getId());
        event.put("customerId", nullSafe(notification.getCustomerId()));
        event.put("channel", nullSafe(notification.getChannel()));
        event.put("retryCount", notification.getRetryCount());
        event.put("failureReason", nullSafe(notification.getFailureReason()));
        event.put("alertOps", true);
        publish(event, "NOTIFICATION_BOUNCED", notification.getId());
    }

    @Override
    public void publishNotificationFailed(Notification notification) {
        Map<String, Object> event = Map.of(
                "eventType", "NOTIFICATION_FAILED",
                "notificationId", notification.getId(),
                "customerId", nullSafe(notification.getCustomerId()),
                "channel", nullSafe(notification.getChannel()),
                "retryCount", notification.getRetryCount(),
                "failureReason", nullSafe(notification.getFailureReason()));
        publish(event, "NOTIFICATION_FAILED", notification.getId());
    }

    private void publish(Object event, String eventType, String notificationId) {
        try {
            String body = objectMapper.writeValueAsString(event);
            chenilePub.publish(KafkaTopics.NOTIFICATION_EVENTS, body,
                    Map.of("key", notificationId, "eventType", eventType));
            log.info("Published {} for id={}", eventType, notificationId);
        } catch (JacksonException e) {
            log.error("Failed to publish {} for id={}", eventType, notificationId, e);
        }
    }

    private String nullSafe(Object value) {
        return value != null ? value.toString() : "";
    }
}
