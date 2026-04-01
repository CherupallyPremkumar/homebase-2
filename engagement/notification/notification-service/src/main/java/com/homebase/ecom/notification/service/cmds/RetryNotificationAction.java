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
 * STM action for retrying a failed notification (FAILED -> CHECK_RETRY auto-state).
 * The CHECK_RETRY auto-state will route to RETRY or BOUNCED based on retryCount.
 */
public class RetryNotificationAction extends AbstractSTMTransitionAction<Notification, RetryNotificationPayload> {

    private static final Logger log = LoggerFactory.getLogger(RetryNotificationAction.class);

    @Override
    public void transitionTo(Notification notification, RetryNotificationPayload payload, State startState,
            String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Reset failure state for re-send
        notification.setFailureReason(null);
        notification.setSentAt(null);

        log.info("Notification {} retry initiated (attempt #{}/3): customerId={}, channel={}",
                notification.getId(), notification.getRetryCount(),
                notification.getCustomerId(), notification.getChannel());
    }
}
