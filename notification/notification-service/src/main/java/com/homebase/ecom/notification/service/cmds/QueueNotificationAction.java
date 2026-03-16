package com.homebase.ecom.notification.service.cmds;

import com.homebase.ecom.notification.domain.model.Notification;
import com.homebase.ecom.notification.dto.QueueNotificationPayload;
import com.homebase.ecom.notification.service.validator.NotificationPolicyValidator;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * STM action for queueing a notification (CREATED -> QUEUED).
 * Runs full policy validation: channel, template, unsubscribe check.
 */
public class QueueNotificationAction extends AbstractSTMTransitionAction<Notification, QueueNotificationPayload> {

    private static final Logger log = LoggerFactory.getLogger(QueueNotificationAction.class);

    @Autowired
    private NotificationPolicyValidator policyValidator;

    @Override
    public void transitionTo(Notification notification, QueueNotificationPayload payload, State startState,
            String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Full policy validation before queueing
        policyValidator.validateForQueue(notification);

        log.info("Notification {} queued for delivery: channel={}, customerId={}, templateId={}",
                notification.getId(), notification.getChannel(),
                notification.getCustomerId(), notification.getTemplateId());

        notification.getTransientMap().put("previousPayload", payload);
    }
}
