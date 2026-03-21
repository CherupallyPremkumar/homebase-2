package com.homebase.ecom.notification.service.postSaveHooks;

import com.homebase.ecom.notification.domain.model.Notification;
import com.homebase.ecom.notification.domain.port.NotificationEventPublisherPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post save hook for SENT state.
 * Publishes NOTIFICATION_SENT event to notification.events topic.
 */
public class SENTNotificationPostSaveHook implements PostSaveHook<Notification> {

    private static final Logger log = LoggerFactory.getLogger(SENTNotificationPostSaveHook.class);

    private final NotificationEventPublisherPort eventPublisher;

    public SENTNotificationPostSaveHook(NotificationEventPublisherPort eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, Notification notification, TransientMap map) {
        log.info("NOTIFICATION_SENT: id={}, channel={}, customerId={}, sentAt={}",
                notification.getId(), notification.getChannel(),
                notification.getCustomerId(), notification.getSentAt());

        eventPublisher.publishNotificationSent(notification);
    }
}
