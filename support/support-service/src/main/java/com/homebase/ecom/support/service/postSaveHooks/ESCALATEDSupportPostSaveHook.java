package com.homebase.ecom.support.service.postSaveHooks;

import com.homebase.ecom.support.model.SupportTicket;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post Save Hook for the ESCALATED state.
 * Publishes a TicketEscalatedEvent when a ticket transitions to ESCALATED.
 */
public class ESCALATEDSupportPostSaveHook implements PostSaveHook<SupportTicket> {

    private static final Logger log = LoggerFactory.getLogger(ESCALATEDSupportPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, SupportTicket ticket, TransientMap map) {
        log.info("TicketEscalatedEvent: Ticket {} escalated from state {} to ESCALATED. Priority: {}",
                ticket.getId(),
                startState != null ? startState.getStateId() : "null",
                ticket.getPriority());

        map.put("eventType", "TicketEscalatedEvent");
        map.put("escalatedPriority", ticket.getPriority());
    }
}
