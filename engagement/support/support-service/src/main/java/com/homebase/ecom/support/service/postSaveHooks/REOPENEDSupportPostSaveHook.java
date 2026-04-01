package com.homebase.ecom.support.service.postSaveHooks;

import com.homebase.ecom.support.model.SupportTicket;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post Save Hook for the REOPENED state.
 * Logs ticket reopening with reopen count.
 */
public class REOPENEDSupportPostSaveHook implements PostSaveHook<SupportTicket> {

    private static final Logger log = LoggerFactory.getLogger(REOPENEDSupportPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, SupportTicket ticket, TransientMap map) {
        log.info("TicketReopenedEvent: Ticket {} reopened. Reopen count: {}, Previous state: {}",
                ticket.getId(), ticket.getReopenCount(),
                startState != null ? startState.getStateId() : "null");
        map.put("eventType", "TicketReopenedEvent");
        map.put("reopenCount", ticket.getReopenCount());
    }
}
