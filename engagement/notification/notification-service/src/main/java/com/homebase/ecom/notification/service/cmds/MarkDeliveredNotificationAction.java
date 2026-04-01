package com.homebase.ecom.notification.service.cmds;

import com.homebase.ecom.notification.domain.model.Notification;
import com.homebase.ecom.notification.dto.MarkDeliveredNotificationPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * STM action for marking a notification as delivered (SENDING -> SENT or SENT -> DELIVERED).
 * Sets the deliveredAt timestamp.
 */
public class MarkDeliveredNotificationAction extends AbstractSTMTransitionAction<Notification, MarkDeliveredNotificationPayload> {

    private static final Logger log = LoggerFactory.getLogger(MarkDeliveredNotificationAction.class);

    @Override
    public void transitionTo(Notification notification, MarkDeliveredNotificationPayload payload, State startState,
            String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        notification.setDeliveredAt(new Date());

        log.info("Notification {} delivery confirmed: customerId={}, channel={}, deliveredAt={}",
                notification.getId(), notification.getCustomerId(),
                notification.getChannel(), notification.getDeliveredAt());
    }
}
