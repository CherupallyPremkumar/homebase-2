package com.homebase.ecom.support.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.support.model.SupportTicket;
import com.homebase.ecom.support.model.TicketMessage;
import com.homebase.ecom.support.dto.ResolveTicketPayload;

import java.util.Date;

public class ResolveSupportAction extends AbstractSTMTransitionAction<SupportTicket, ResolveTicketPayload> {

    @Override
    public void transitionTo(SupportTicket ticket,
            ResolveTicketPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (payload.getResolution() == null || payload.getResolution().trim().isEmpty()) {
            throw new IllegalArgumentException("Resolution description is required");
        }

        Date now = new Date();
        ticket.setResolvedAt(now);

        // Store resolution timestamp for duration calculations
        ticket.getTransientMap().put("resolvedAt", now);

        // Append resolution to description
        String desc = ticket.getDescription() != null ? ticket.getDescription() : "";
        ticket.setDescription(desc + "\nResolution: " + payload.getResolution());

        // Add resolution message
        TicketMessage resolveMsg = new TicketMessage();
        resolveMsg.setId(java.util.UUID.randomUUID().toString());
        resolveMsg.setSenderType("SYSTEM");
        resolveMsg.setMessage("Ticket resolved. Resolution: " + payload.getResolution());
        ticket.getMessages().add(resolveMsg);

        ticket.getTransientMap().previousPayload = payload;
    }
}
