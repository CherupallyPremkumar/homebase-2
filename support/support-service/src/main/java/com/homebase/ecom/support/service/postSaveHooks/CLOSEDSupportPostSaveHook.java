package com.homebase.ecom.support.service.postSaveHooks;

import com.homebase.ecom.support.model.SupportTicket;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post Save Hook for the CLOSED state.
 * Logs final ticket closure and archives ticket data.
 */
public class CLOSEDSupportPostSaveHook implements PostSaveHook<SupportTicket> {

    private static final Logger log = LoggerFactory.getLogger(CLOSEDSupportPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, SupportTicket ticket, TransientMap map) {
        log.info("TicketClosedEvent: Ticket {} closed. Total messages: {}",
                ticket.getId(), ticket.getMessages().size());
        map.put("eventType", "TicketClosedEvent");
    }
}
