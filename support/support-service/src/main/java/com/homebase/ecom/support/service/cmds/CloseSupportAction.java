package com.homebase.ecom.support.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.support.model.SupportTicket;
import com.homebase.ecom.support.model.TicketMessage;
import com.homebase.ecom.support.dto.CloseTicketPayload;

/**
 * Closes a support ticket from RESOLVED to CLOSED.
 */
public class CloseSupportAction extends AbstractSTMTransitionAction<SupportTicket, CloseTicketPayload> {

    @Override
    public void transitionTo(SupportTicket ticket,
            CloseTicketPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Ensure the ticket was resolved before closing
        if (ticket.getResolvedAt() == null) {
            throw new IllegalStateException("Cannot close a ticket that has not been resolved");
        }

        // Add system message about closure
        TicketMessage closeMsg = new TicketMessage();
        closeMsg.setId(java.util.UUID.randomUUID().toString());
        closeMsg.setSenderType("SYSTEM");
        closeMsg.setMessage("Ticket closed after resolution period.");
        ticket.getMessages().add(closeMsg);

        ticket.getTransientMap().previousPayload = payload;
    }
}
