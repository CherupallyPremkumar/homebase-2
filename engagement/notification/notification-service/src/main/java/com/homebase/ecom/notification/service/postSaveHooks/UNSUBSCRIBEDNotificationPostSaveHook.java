package com.homebase.ecom.notification.service.postSaveHooks;

import com.homebase.ecom.notification.domain.model.Notification;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post save hook for UNSUBSCRIBED state.
 * Customer has opted out of this notification channel.
 */
public class UNSUBSCRIBEDNotificationPostSaveHook implements PostSaveHook<Notification> {

    private static final Logger log = LoggerFactory.getLogger(UNSUBSCRIBEDNotificationPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Notification notification, TransientMap map) {
        log.info("Customer {} unsubscribed from notification {}: channel={}",
                notification.getCustomerId(), notification.getId(), notification.getChannel());
    }
}
