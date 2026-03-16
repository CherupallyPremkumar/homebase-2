package com.homebase.ecom.notification.service.postSaveHooks;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.notification.domain.model.Notification;
import org.chenile.pubsub.ChenilePub;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Post save hook for BOUNCED state (Item 13: NotificationFailedHook — alert ops if retries exhausted).
 * Publishes NOTIFICATION_BOUNCED event to notification.events topic.
 * This is the ops-alerting hook: all retries are exhausted, permanent failure.
 */
public class BOUNCEDNotificationPostSaveHook implements PostSaveHook<Notification> {

    private static final Logger log = LoggerFactory.getLogger(BOUNCEDNotificationPostSaveHook.class);

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void execute(State startState, State endState, Notification notification, TransientMap map) {
        log.error("NOTIFICATION_BOUNCED: id={}, retries exhausted ({}), channel={}, customerId={}, failureReason={}. ALERTING OPS.",
                notification.getId(), notification.getRetryCount(),
                notification.getChannel(), notification.getCustomerId(),
                notification.getFailureReason());

        if (chenilePub == null) return;

        try {
            Map<String, Object> event = Map.of(
                    "eventType", "NOTIFICATION_BOUNCED",
                    "notificationId", notification.getId(),
                    "customerId", notification.getCustomerId() != null ? notification.getCustomerId() : "",
                    "channel", notification.getChannel() != null ? notification.getChannel() : "",
                    "retryCount", notification.getRetryCount(),
                    "failureReason", notification.getFailureReason() != null ? notification.getFailureReason() : "",
                    "alertOps", true
            );
            String body = objectMapper.writeValueAsString(event);
            chenilePub.publish("notification.events", body,
                    Map.of("key", notification.getId(), "eventType", "NOTIFICATION_BOUNCED"));
        } catch (JacksonException e) {
            log.error("Failed to publish NOTIFICATION_BOUNCED for id={}", notification.getId(), e);
        }
    }
}
