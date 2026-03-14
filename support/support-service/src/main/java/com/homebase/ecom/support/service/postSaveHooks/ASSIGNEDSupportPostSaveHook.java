package com.homebase.ecom.support.service.postSaveHooks;

import com.homebase.ecom.support.model.SupportTicket;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post Save Hook for the ASSIGNED state.
 * Publishes a TicketAssignedEvent when a ticket transitions to ASSIGNED.
 */
public class ASSIGNEDSupportPostSaveHook implements PostSaveHook<SupportTicket> {

    private static final Logger log = LoggerFactory.getLogger(ASSIGNEDSupportPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, SupportTicket ticket, TransientMap map) {
        log.info("TicketAssignedEvent: Ticket {} assigned to agent {} (priority: {}, from state: {})",
                ticket.getId(), ticket.getAssignedTo(), ticket.getPriority(),
                startState != null ? startState.getStateId() : "null");

        // Store event info for downstream consumers
        map.put("eventType", "TicketAssignedEvent");
        map.put("assignedTo", ticket.getAssignedTo());

        Object slaDeadline = map.get("slaDeadline");
        if (slaDeadline != null) {
            log.info("SLA deadline set for ticket {}: {}", ticket.getId(), slaDeadline);
        }
    }
}
