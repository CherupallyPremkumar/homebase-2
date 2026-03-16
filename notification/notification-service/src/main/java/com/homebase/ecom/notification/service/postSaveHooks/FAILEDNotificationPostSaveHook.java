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
 * Post save hook for FAILED state.
 * Publishes NOTIFICATION_FAILED event to notification.events topic.
 */
public class FAILEDNotificationPostSaveHook implements PostSaveHook<Notification> {

    private static final Logger log = LoggerFactory.getLogger(FAILEDNotificationPostSaveHook.class);

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void execute(State startState, State endState, Notification notification, TransientMap map) {
        log.warn("NOTIFICATION_FAILED: id={}, attempt #{}, channel={}, reason={}",
                notification.getId(), notification.getRetryCount(),
                notification.getChannel(), notification.getFailureReason());

        if (chenilePub == null) return;

        try {
            Map<String, Object> event = Map.of(
                    "eventType", "NOTIFICATION_FAILED",
                    "notificationId", notification.getId(),
                    "customerId", notification.getCustomerId() != null ? notification.getCustomerId() : "",
                    "channel", notification.getChannel() != null ? notification.getChannel() : "",
                    "retryCount", notification.getRetryCount(),
                    "failureReason", notification.getFailureReason() != null ? notification.getFailureReason() : ""
            );
            String body = objectMapper.writeValueAsString(event);
            chenilePub.publish("notification.events", body,
                    Map.of("key", notification.getId(), "eventType", "NOTIFICATION_FAILED"));
        } catch (JacksonException e) {
            log.error("Failed to publish NOTIFICATION_FAILED for id={}", notification.getId(), e);
        }
    }
}
