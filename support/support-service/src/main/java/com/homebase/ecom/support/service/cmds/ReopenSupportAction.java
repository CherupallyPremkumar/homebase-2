package com.homebase.ecom.support.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.support.model.SupportTicket;
import com.homebase.ecom.support.model.TicketMessage;
import com.homebase.ecom.support.dto.ReopenTicketPayload;

public class ReopenSupportAction extends AbstractSTMTransitionAction<SupportTicket, ReopenTicketPayload> {

    @Override
    public void transitionTo(SupportTicket ticket,
            ReopenTicketPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (payload.getReason() == null || payload.getReason().trim().isEmpty()) {
            throw new IllegalArgumentException("Reason for reopening is required");
        }

        // Clear resolved data
        ticket.setResolvedAt(null);
        // Clear assignment so ticket can be reassigned
        ticket.setAssignedTo(null);

        // Add system message about reopening
        TicketMessage reopenMsg = new TicketMessage();
        reopenMsg.setId(java.util.UUID.randomUUID().toString());
        reopenMsg.setSenderType("SYSTEM");
        reopenMsg.setMessage("Ticket reopened. Reason: " + payload.getReason());
        ticket.getMessages().add(reopenMsg);

        ticket.getTransientMap().previousPayload = payload;
    }
}
