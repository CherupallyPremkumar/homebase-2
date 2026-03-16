package com.homebase.ecom.support.service.postSaveHooks;

import com.homebase.ecom.support.domain.port.NotificationPort;
import com.homebase.ecom.support.model.SupportTicket;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Post Save Hook for the ASSIGNED state.
 * Notifies the assigned agent about the new ticket.
 */
public class ASSIGNEDSupportPostSaveHook implements PostSaveHook<SupportTicket> {

    private static final Logger log = LoggerFactory.getLogger(ASSIGNEDSupportPostSaveHook.class);

    @Autowired(required = false)
    private NotificationPort notificationPort;

    @Override
    public void execute(State startState, State endState, SupportTicket ticket, TransientMap map) {
        log.info("TicketAssignedEvent: Ticket {} assigned to agent {} (priority: {}, from state: {})",
                ticket.getId(), ticket.getAssignedAgentId(), ticket.getPriority(),
                startState != null ? startState.getStateId() : "null");

        // Notify agent about assignment
        if (notificationPort != null && ticket.getAssignedAgentId() != null) {
            notificationPort.notifyAgentAssignment(
                    ticket.getAssignedAgentId(), ticket.getId(),
                    ticket.getSubject(), ticket.getPriority());
        }

        map.put("eventType", "TicketAssignedEvent");
        map.put("assignedTo", ticket.getAssignedAgentId());

        Object slaDeadline = map.get("slaDeadline");
        if (slaDeadline != null) {
            log.info("SLA deadline set for ticket {}: {}", ticket.getId(), slaDeadline);
        }
    }
}
