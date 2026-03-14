package com.homebase.ecom.notification.service.cmds;

import com.homebase.ecom.notification.domain.model.Notification;
import com.homebase.ecom.notification.dto.RetryNotificationPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for retrying a failed notification (FAILED -> CREATED).
 * Resets error state and checks maximum retry limits.
 */
public class RetryNotificationAction extends AbstractSTMTransitionAction<Notification, RetryNotificationPayload> {

    private static final Logger log = LoggerFactory.getLogger(RetryNotificationAction.class);
    private static final int MAX_RETRIES = 3;

    @Override
    public void transitionTo(Notification notification, RetryNotificationPayload payload, State startState,
            String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Check max retry limit
        if (notification.getRetryCount() >= MAX_RETRIES) {
            throw new IllegalStateException("Maximum retry limit (" + MAX_RETRIES
                    + ") exceeded for notification " + notification.getId());
        }

        // Reset for re-send
        notification.setErrorMessage(null);
        notification.setSentAt(null);

        log.info("Notification {} queued for retry (attempt #{}/{})",
                notification.getId(), notification.getRetryCount() + 1, MAX_RETRIES);
    }
}
