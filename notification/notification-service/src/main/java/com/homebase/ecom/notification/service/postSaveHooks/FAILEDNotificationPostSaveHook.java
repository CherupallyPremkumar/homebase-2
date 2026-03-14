package com.homebase.ecom.notification.service.postSaveHooks;

import com.homebase.ecom.notification.domain.model.Notification;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post save hook for FAILED state.
 * Publishes a NotificationFailedEvent for alerting and retry processing.
 */
public class FAILEDNotificationPostSaveHook implements PostSaveHook<Notification> {

    private static final Logger log = LoggerFactory.getLogger(FAILEDNotificationPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Notification notification, TransientMap map) {
        log.warn("NotificationFailedEvent: Notification {} failed (attempt #{}) via {}. Error: {}",
                notification.getId(), notification.getRetryCount(),
                notification.getChannel(), notification.getErrorMessage());

        map.put("eventType", "NotificationFailedEvent");
        map.put("errorMessage", notification.getErrorMessage());
        map.put("retryCount", notification.getRetryCount());
    }
}
