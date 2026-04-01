package com.homebase.ecom.notification.service.postSaveHooks;

import com.homebase.ecom.notification.domain.model.Notification;
import com.homebase.ecom.notification.domain.port.NotificationEventPublisherPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post save hook for BOUNCED state (Item 13: NotificationFailedHook -- alert ops if retries exhausted).
 * Publishes NOTIFICATION_BOUNCED event to notification.events topic.
 * This is the ops-alerting hook: all retries are exhausted, permanent failure.
 */
public class BOUNCEDNotificationPostSaveHook implements PostSaveHook<Notification> {

    private static final Logger log = LoggerFactory.getLogger(BOUNCEDNotificationPostSaveHook.class);

    private final NotificationEventPublisherPort eventPublisher;

    public BOUNCEDNotificationPostSaveHook(NotificationEventPublisherPort eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, Notification notification, TransientMap map) {
        log.error("NOTIFICATION_BOUNCED: id={}, retries exhausted ({}), channel={}, customerId={}, failureReason={}. ALERTING OPS.",
                notification.getId(), notification.getRetryCount(),
                notification.getChannel(), notification.getCustomerId(),
                notification.getFailureReason());

        eventPublisher.publishNotificationBounced(notification);
    }
}
