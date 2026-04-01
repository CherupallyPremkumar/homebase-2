package com.homebase.ecom.notification.service.postSaveHooks;

import com.homebase.ecom.notification.domain.model.Notification;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post save hook for RETRY state.
 * Notification is queued for re-delivery.
 */
public class RETRYNotificationPostSaveHook implements PostSaveHook<Notification> {

    private static final Logger log = LoggerFactory.getLogger(RETRYNotificationPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Notification notification, TransientMap map) {
        log.info("Notification {} queued for retry (attempt #{}): channel={}, customerId={}",
                notification.getId(), notification.getRetryCount(),
                notification.getChannel(), notification.getCustomerId());
    }
}
