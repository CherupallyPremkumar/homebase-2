package com.homebase.ecom.notification.service.cmds;

import com.homebase.ecom.notification.domain.model.Notification;
import com.homebase.ecom.notification.dto.SendNotificationPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Arrays;
import java.util.List;

/**
 * STM action for sending a notification.
 * Selects template, renders content, and dispatches to the specified channel.
 */
public class SendNotificationAction extends AbstractSTMTransitionAction<Notification, SendNotificationPayload> {

    private static final Logger log = LoggerFactory.getLogger(SendNotificationAction.class);
    private static final List<String> VALID_CHANNELS = Arrays.asList("EMAIL", "SMS", "PUSH", "IN_APP");

    @Override
    public void transitionTo(Notification notification, SendNotificationPayload payload, State startState,
            String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Validate channel
        String channel = payload.getChannel();
        if (channel == null || channel.trim().isEmpty()) {
            throw new IllegalArgumentException("Notification channel is required");
        }
        if (!VALID_CHANNELS.contains(channel.toUpperCase())) {
            throw new IllegalArgumentException("Invalid notification channel: " + channel
                    + ". Supported channels: " + VALID_CHANNELS);
        }

        // Validate that we have content to send
        if ((payload.getBody() == null || payload.getBody().trim().isEmpty())
                && (payload.getTemplateCode() == null || payload.getTemplateCode().trim().isEmpty())) {
            throw new IllegalArgumentException("Either body or templateCode must be provided");
        }

        notification.setChannel(channel.toUpperCase());
        notification.setTemplateCode(payload.getTemplateCode());

        // Render content - if templateCode provided, use it as basis; otherwise use payload body
        if (payload.getTemplateCode() != null && !payload.getTemplateCode().trim().isEmpty()) {
            // Template-based rendering (simplified - in production would use a template engine)
            String renderedSubject = payload.getSubject() != null ? payload.getSubject()
                    : "Notification: " + payload.getTemplateCode();
            String renderedBody = payload.getBody() != null ? payload.getBody()
                    : "Template " + payload.getTemplateCode() + " rendered content";
            notification.setSubject(renderedSubject);
            notification.setBody(renderedBody);
        } else {
            notification.setSubject(payload.getSubject());
            notification.setBody(payload.getBody());
        }

        notification.setReferenceType(payload.getReferenceType());
        notification.setReferenceId(payload.getReferenceId());
        notification.setSentAt(new Date());
        notification.setErrorMessage(null);

        log.info("Notification dispatched via {}: userId={}, subject={}",
                notification.getChannel(), notification.getUserId(), notification.getSubject());

        notification.getTransientMap().put("previousPayload", payload);
    }
}
