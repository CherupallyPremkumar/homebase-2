package com.homebase.ecom.support.service.postSaveHooks;

import com.homebase.ecom.support.model.SupportTicket;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post Save Hook for the IN_PROGRESS state.
 */
public class IN_PROGRESSSupportPostSaveHook implements PostSaveHook<SupportTicket> {

    private static final Logger log = LoggerFactory.getLogger(IN_PROGRESSSupportPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, SupportTicket ticket, TransientMap map) {
        log.info("TicketInProgressEvent: Ticket {} now in progress. Agent: {}",
                ticket.getId(), ticket.getAssignedAgentId());
        map.put("eventType", "TicketInProgressEvent");
    }
}
