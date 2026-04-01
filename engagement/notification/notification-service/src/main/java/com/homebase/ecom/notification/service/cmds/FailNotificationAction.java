package com.homebase.ecom.notification.service.cmds;

import com.homebase.ecom.notification.domain.model.Notification;
import com.homebase.ecom.notification.dto.FailNotificationPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for marking a notification as failed (SENDING -> FAILED).
 * Records the failure reason and increments the retry count.
 */
public class FailNotificationAction extends AbstractSTMTransitionAction<Notification, FailNotificationPayload> {

    private static final Logger log = LoggerFactory.getLogger(FailNotificationAction.class);

    @Override
    public void transitionTo(Notification notification, FailNotificationPayload payload, State startState,
            String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        String failureReason = payload.getFailureReason() != null ? payload.getFailureReason()
                : (payload.getComment() != null ? payload.getComment() : "Delivery failed");
        notification.setFailureReason(failureReason);
        notification.setRetryCount(notification.getRetryCount() + 1);

        log.warn("Notification {} failed (attempt #{}): channel={}, reason={}",
                notification.getId(), notification.getRetryCount(),
                notification.getChannel(), failureReason);

        notification.getTransientMap().put("previousPayload", payload);
    }
}
