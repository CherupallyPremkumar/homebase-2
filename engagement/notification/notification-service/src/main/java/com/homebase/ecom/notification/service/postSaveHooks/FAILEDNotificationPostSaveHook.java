package com.homebase.ecom.notification.service.postSaveHooks;

import com.homebase.ecom.notification.domain.model.Notification;
import com.homebase.ecom.notification.domain.port.NotificationEventPublisherPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post save hook for FAILED state.
 * Publishes NOTIFICATION_FAILED event to notification.events topic.
 */
public class FAILEDNotificationPostSaveHook implements PostSaveHook<Notification> {

    private static final Logger log = LoggerFactory.getLogger(FAILEDNotificationPostSaveHook.class);

    private final NotificationEventPublisherPort eventPublisher;

    public FAILEDNotificationPostSaveHook(NotificationEventPublisherPort eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, Notification notification, TransientMap map) {
        log.warn("NOTIFICATION_FAILED: id={}, attempt #{}, channel={}, reason={}",
                notification.getId(), notification.getRetryCount(),
                notification.getChannel(), notification.getFailureReason());

        eventPublisher.publishNotificationFailed(notification);
    }
}
