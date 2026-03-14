package com.homebase.ecom.notification.service.postSaveHooks;

import com.homebase.ecom.notification.domain.model.Notification;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post save hook for SENT state.
 * Publishes a NotificationSentEvent for downstream consumers.
 */
public class SENTNotificationPostSaveHook implements PostSaveHook<Notification> {

    private static final Logger log = LoggerFactory.getLogger(SENTNotificationPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Notification notification, TransientMap map) {
        log.info("NotificationSentEvent: Notification {} sent via {} to user {} at {}",
                notification.getId(), notification.getChannel(),
                notification.getUserId(), notification.getSentAt());

        map.put("eventType", "NotificationSentEvent");
        map.put("channel", notification.getChannel());
        map.put("sentAt", notification.getSentAt());
    }
}
