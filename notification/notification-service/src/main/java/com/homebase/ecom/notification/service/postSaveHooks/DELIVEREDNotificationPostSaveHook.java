package com.homebase.ecom.notification.service.postSaveHooks;

import com.homebase.ecom.notification.domain.model.Notification;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post save hook for DELIVERED state.
 * Logs confirmed delivery.
 */
public class DELIVEREDNotificationPostSaveHook implements PostSaveHook<Notification> {

    private static final Logger log = LoggerFactory.getLogger(DELIVEREDNotificationPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Notification notification, TransientMap map) {
        log.info("NOTIFICATION_DELIVERED: id={}, channel={}, customerId={}, deliveredAt={}",
                notification.getId(), notification.getChannel(),
                notification.getCustomerId(), notification.getDeliveredAt());
    }
}
