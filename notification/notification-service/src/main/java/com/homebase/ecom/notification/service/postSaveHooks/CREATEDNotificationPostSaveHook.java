package com.homebase.ecom.notification.service.postSaveHooks;

import com.homebase.ecom.notification.domain.model.Notification;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post save hook for CREATED state.
 * Logs notification creation and prepares for dispatch.
 */
public class CREATEDNotificationPostSaveHook implements PostSaveHook<Notification> {

    private static final Logger log = LoggerFactory.getLogger(CREATEDNotificationPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Notification notification, TransientMap map) {
        if (startState == null) {
            log.info("NotificationCreatedEvent: New notification {} created for user {}",
                    notification.getId(), notification.getUserId());
        } else {
            log.info("NotificationRetryEvent: Notification {} queued for retry (from FAILED)",
                    notification.getId());
        }
        map.put("eventType", startState == null ? "NotificationCreatedEvent" : "NotificationRetryEvent");
    }
}
