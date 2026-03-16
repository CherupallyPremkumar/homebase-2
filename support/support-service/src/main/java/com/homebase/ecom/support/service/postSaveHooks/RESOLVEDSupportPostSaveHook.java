package com.homebase.ecom.support.service.postSaveHooks;

import com.homebase.ecom.support.domain.port.NotificationPort;
import com.homebase.ecom.support.model.SupportTicket;
import com.homebase.ecom.shared.event.KafkaTopics;
import org.chenile.pubsub.ChenilePub;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Post Save Hook for the RESOLVED state — TicketResolvedHook.
 * Notifies customer about resolution and sends satisfaction survey.
 * Publishes TICKET_RESOLVED to support.events.
 */
public class RESOLVEDSupportPostSaveHook implements PostSaveHook<SupportTicket> {

    private static final Logger log = LoggerFactory.getLogger(RESOLVEDSupportPostSaveHook.class);

    @Autowired(required = false)
    private NotificationPort notificationPort;

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Override
    public void execute(State startState, State endState, SupportTicket ticket, TransientMap map) {
        log.info("TICKET_RESOLVED: Ticket {} resolved at {} by agent {}",
                ticket.getId(), ticket.getResolvedAt(), ticket.getAssignedAgentId());

        // Notify customer about resolution
        if (notificationPort != null && ticket.getCustomerId() != null) {
            notificationPort.notifyCustomerResolution(
                    ticket.getCustomerId(), ticket.getId(),
                    ticket.getSubject(), ticket.getDescription());

            // Send satisfaction survey
            notificationPort.sendSatisfactionSurvey(ticket.getCustomerId(), ticket.getId());
        }

        // Publish TICKET_RESOLVED event
        if (chenilePub != null) {
            try {
                String body = "{\"ticketId\":\"" + ticket.getId()
                        + "\",\"customerId\":\"" + ticket.getCustomerId()
                        + "\",\"assignedAgentId\":\"" + ticket.getAssignedAgentId()
                        + "\",\"eventType\":\"TICKET_RESOLVED\"}";
                chenilePub.publish(KafkaTopics.SUPPORT_EVENTS, body,
                        Map.of("key", ticket.getId(), "eventType", "TICKET_RESOLVED"));
            } catch (Exception e) {
                log.error("Failed to publish TICKET_RESOLVED for ticket {}", ticket.getId(), e);
            }
        }

        map.put("eventType", "TICKET_RESOLVED");
        map.put("resolvedAt", ticket.getResolvedAt());
    }
}
