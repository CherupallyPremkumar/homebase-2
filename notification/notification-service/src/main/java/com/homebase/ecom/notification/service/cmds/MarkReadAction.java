package com.homebase.ecom.notification.service.cmds;

import com.homebase.ecom.notification.domain.model.Notification;
import com.homebase.ecom.notification.dto.MarkReadPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * STM action for marking a notification as read (SENT -> READ).
 * Sets the readAt timestamp.
 */
public class MarkReadAction extends AbstractSTMTransitionAction<Notification, MarkReadPayload> {

    private static final Logger log = LoggerFactory.getLogger(MarkReadAction.class);

    @Override
    public void transitionTo(Notification notification, MarkReadPayload payload, State startState,
            String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        notification.setReadAt(new Date());

        log.info("Notification {} marked as read by user {} at {}",
                notification.getId(), notification.getUserId(), notification.getReadAt());
    }
}
