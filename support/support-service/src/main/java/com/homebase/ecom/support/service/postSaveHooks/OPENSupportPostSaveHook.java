package com.homebase.ecom.support.service.postSaveHooks;

import com.homebase.ecom.support.domain.port.AgentAssignmentPort;
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
 * Post Save Hook for the OPEN state — TicketCreatedHook.
 * On new ticket creation: attempts auto-assignment and notifies agent.
 * On reopen: logs the reopening.
 * Publishes TICKET_CREATED to support.events.
 */
public class OPENSupportPostSaveHook implements PostSaveHook<SupportTicket> {

    private static final Logger log = LoggerFactory.getLogger(OPENSupportPostSaveHook.class);

    @Autowired(required = false)
    private AgentAssignmentPort agentAssignmentPort;

    @Autowired(required = false)
    private NotificationPort notificationPort;

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Override
    public void execute(State startState, State endState, SupportTicket ticket, TransientMap map) {
        if (startState == null) {
            // New ticket created
            log.info("TICKET_CREATED: New ticket {} created. Subject: {}, Priority: {}, Category: {}",
                    ticket.getId(), ticket.getSubject(), ticket.getPriority(), ticket.getCategory());

            // Attempt auto-assignment
            if (agentAssignmentPort != null) {
                String agentId = agentAssignmentPort.findAvailableAgent(ticket.getCategory(), ticket.getPriority());
                if (agentId != null) {
                    log.info("Auto-assigning ticket {} to agent {}", ticket.getId(), agentId);
                    map.put("autoAssignAgent", agentId);
                }
            }

            // Publish TICKET_CREATED event
            if (chenilePub != null) {
                try {
                    String body = "{\"ticketId\":\"" + ticket.getId()
                            + "\",\"customerId\":\"" + ticket.getCustomerId()
                            + "\",\"category\":\"" + ticket.getCategory()
                            + "\",\"priority\":\"" + ticket.getPriority()
                            + "\",\"subject\":\"" + ticket.getSubject()
                            + "\",\"eventType\":\"TICKET_CREATED\"}";
                    chenilePub.publish(KafkaTopics.SUPPORT_EVENTS, body,
                            Map.of("key", ticket.getId(), "eventType", "TICKET_CREATED"));
                } catch (Exception e) {
                    log.error("Failed to publish TICKET_CREATED for ticket {}", ticket.getId(), e);
                }
            }

            map.put("eventType", "TICKET_CREATED");
        } else {
            log.info("TicketReopenedEvent: Ticket {} reopened from state {}",
                    ticket.getId(), startState.getStateId());
            map.put("eventType", "TicketReopenedEvent");
        }
    }
}
