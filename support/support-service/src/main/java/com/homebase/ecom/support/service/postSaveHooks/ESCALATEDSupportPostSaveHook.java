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
 * Post Save Hook for the ESCALATED state — TicketEscalatedHook.
 * Notifies supervisor about escalation.
 * Publishes TICKET_ESCALATED to support.events.
 */
public class ESCALATEDSupportPostSaveHook implements PostSaveHook<SupportTicket> {

    private static final Logger log = LoggerFactory.getLogger(ESCALATEDSupportPostSaveHook.class);

    @Autowired(required = false)
    private NotificationPort notificationPort;

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Override
    public void execute(State startState, State endState, SupportTicket ticket, TransientMap map) {
        log.info("TICKET_ESCALATED: Ticket {} escalated from state {} to ESCALATED. Priority: {}",
                ticket.getId(),
                startState != null ? startState.getStateId() : "null",
                ticket.getPriority());

        // Notify supervisor
        if (notificationPort != null) {
            Object previousPayload = map.previousPayload;
            String escalationReason = previousPayload != null ? previousPayload.toString() : "Escalated";
            notificationPort.notifySupervisorEscalation(
                    ticket.getId(), ticket.getSubject(), ticket.getPriority(), escalationReason);
        }

        // Publish TICKET_ESCALATED event
        if (chenilePub != null) {
            try {
                String body = "{\"ticketId\":\"" + ticket.getId()
                        + "\",\"customerId\":\"" + ticket.getCustomerId()
                        + "\",\"priority\":\"" + ticket.getPriority()
                        + "\",\"eventType\":\"TICKET_ESCALATED\"}";
                chenilePub.publish(KafkaTopics.SUPPORT_EVENTS, body,
                        Map.of("key", ticket.getId(), "eventType", "TICKET_ESCALATED"));
            } catch (Exception e) {
                log.error("Failed to publish TICKET_ESCALATED for ticket {}", ticket.getId(), e);
            }
        }

        map.put("eventType", "TICKET_ESCALATED");
        map.put("escalatedPriority", ticket.getPriority());
    }
}
