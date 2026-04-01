package com.homebase.ecom.notification.service.cmds;

import com.homebase.ecom.notification.domain.model.Notification;
import com.homebase.ecom.notification.domain.port.EmailPort;
import com.homebase.ecom.notification.domain.port.PushPort;
import com.homebase.ecom.notification.domain.port.SmsPort;
import com.homebase.ecom.notification.dto.SendNotificationPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * STM action for sending a notification (QUEUED -> SENDING or RETRY -> SENDING).
 * Dispatches to the appropriate channel adapter (EmailPort, SmsPort, PushPort).
 * IN_APP notifications are handled internally without an external adapter.
 */
public class SendNotificationAction extends AbstractSTMTransitionAction<Notification, SendNotificationPayload> {

    private static final Logger log = LoggerFactory.getLogger(SendNotificationAction.class);

    @Autowired(required = false)
    private EmailPort emailPort;

    @Autowired(required = false)
    private SmsPort smsPort;

    @Autowired(required = false)
    private PushPort pushPort;

    @Override
    public void transitionTo(Notification notification, SendNotificationPayload payload, State startState,
            String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        notification.setSentAt(new Date());
        notification.setFailureReason(null);

        // Dispatch to channel adapter
        String channel = notification.getChannel();
        switch (channel) {
            case "EMAIL":
                if (emailPort != null) emailPort.send(notification);
                break;
            case "SMS":
                if (smsPort != null) smsPort.send(notification);
                break;
            case "PUSH":
                if (pushPort != null) pushPort.send(notification);
                break;
            case "IN_APP":
                // IN_APP notifications are stored and queried — no external dispatch needed
                break;
            default:
                log.warn("Unknown channel {} for notification {}, treating as IN_APP",
                        channel, notification.getId());
        }

        log.info("Notification {} dispatched via {}: customerId={}, subject={}",
                notification.getId(), channel, notification.getCustomerId(), notification.getSubject());

        notification.getTransientMap().put("previousPayload", payload);
    }
}
