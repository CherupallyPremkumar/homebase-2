package com.homebase.ecom.support.service.postSaveHooks;

import com.homebase.ecom.support.domain.port.AgentAssignmentPort;
import com.homebase.ecom.support.domain.port.NotificationPort;
import com.homebase.ecom.support.domain.port.SupportEventPublisherPort;
import com.homebase.ecom.support.model.SupportTicket;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Post Save Hook for the OPEN state -- TicketCreatedHook.
 * On new ticket creation: attempts auto-assignment and notifies agent.
 * On reopen: logs the reopening.
 * Publishes TICKET_CREATED via domain port.
 */
public class OPENSupportPostSaveHook implements PostSaveHook<SupportTicket> {

    private static final Logger log = LoggerFactory.getLogger(OPENSupportPostSaveHook.class);

    @Autowired
    private AgentAssignmentPort agentAssignmentPort;

    @Autowired
    private NotificationPort notificationPort;

    @Autowired
    private SupportEventPublisherPort supportEventPublisherPort;

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

            // Publish TICKET_CREATED event via domain port
            if (supportEventPublisherPort != null) {
                supportEventPublisherPort.publishTicketCreated(ticket);
            }

            map.put("eventType", "TICKET_CREATED");
        } else {
            log.info("TicketReopenedEvent: Ticket {} reopened from state {}",
                    ticket.getId(), startState.getStateId());
            map.put("eventType", "TicketReopenedEvent");
        }
    }
}
