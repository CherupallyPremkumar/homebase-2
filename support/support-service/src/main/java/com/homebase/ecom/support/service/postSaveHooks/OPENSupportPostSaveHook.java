package com.homebase.ecom.support.service.postSaveHooks;

import com.homebase.ecom.support.model.SupportTicket;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post Save Hook for the OPEN state.
 * Logs ticket creation or reopening.
 */
public class OPENSupportPostSaveHook implements PostSaveHook<SupportTicket> {

    private static final Logger log = LoggerFactory.getLogger(OPENSupportPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, SupportTicket ticket, TransientMap map) {
        if (startState == null) {
            log.info("TicketCreatedEvent: New ticket {} created. Subject: {}, Priority: {}",
                    ticket.getId(), ticket.getSubject(), ticket.getPriority());
        } else {
            log.info("TicketReopenedEvent: Ticket {} reopened from state {}",
                    ticket.getId(), startState.getStateId());
        }
        map.put("eventType", startState == null ? "TicketCreatedEvent" : "TicketReopenedEvent");
    }
}
