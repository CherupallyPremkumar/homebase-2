package com.homebase.ecom.support.service.postSaveHooks;

import com.homebase.ecom.support.model.SupportTicket;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post Save Hook for the RESOLVED state.
 * Publishes a TicketResolvedEvent when a ticket transitions to RESOLVED.
 */
public class RESOLVEDSupportPostSaveHook implements PostSaveHook<SupportTicket> {

    private static final Logger log = LoggerFactory.getLogger(RESOLVEDSupportPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, SupportTicket ticket, TransientMap map) {
        log.info("TicketResolvedEvent: Ticket {} resolved at {} by agent {}",
                ticket.getId(), ticket.getResolvedAt(), ticket.getAssignedTo());

        map.put("eventType", "TicketResolvedEvent");
        map.put("resolvedAt", ticket.getResolvedAt());
    }
}
