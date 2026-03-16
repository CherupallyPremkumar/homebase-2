package com.homebase.ecom.notification.service.postSaveHooks;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.notification.domain.model.Notification;
import com.homebase.ecom.shared.event.KafkaTopics;
import org.chenile.pubsub.ChenilePub;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Post save hook for SENT state.
 * Publishes NOTIFICATION_SENT event to notification.events topic.
 */
public class SENTNotificationPostSaveHook implements PostSaveHook<Notification> {

    private static final Logger log = LoggerFactory.getLogger(SENTNotificationPostSaveHook.class);

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void execute(State startState, State endState, Notification notification, TransientMap map) {
        log.info("NOTIFICATION_SENT: id={}, channel={}, customerId={}, sentAt={}",
                notification.getId(), notification.getChannel(),
                notification.getCustomerId(), notification.getSentAt());

        if (chenilePub == null) return;

        try {
            Map<String, Object> event = Map.of(
                    "eventType", "NOTIFICATION_SENT",
                    "notificationId", notification.getId(),
                    "customerId", notification.getCustomerId() != null ? notification.getCustomerId() : "",
                    "channel", notification.getChannel() != null ? notification.getChannel() : "",
                    "templateId", notification.getTemplateId() != null ? notification.getTemplateId() : ""
            );
            String body = objectMapper.writeValueAsString(event);
            chenilePub.publish("notification.events", body,
                    Map.of("key", notification.getId(), "eventType", "NOTIFICATION_SENT"));
        } catch (JacksonException e) {
            log.error("Failed to publish NOTIFICATION_SENT for id={}", notification.getId(), e);
        }
    }
}
