package com.homebase.ecom.notification.service.postSaveHooks;

import com.homebase.ecom.notification.domain.model.Notification;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post save hook for READ state.
 * Logs notification read confirmation.
 */
public class READNotificationPostSaveHook implements PostSaveHook<Notification> {

    private static final Logger log = LoggerFactory.getLogger(READNotificationPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Notification notification, TransientMap map) {
        log.info("NotificationReadEvent: Notification {} read by user {} at {}",
                notification.getId(), notification.getUserId(), notification.getReadAt());
        map.put("eventType", "NotificationReadEvent");
    }
}
