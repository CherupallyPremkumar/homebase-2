package com.homebase.ecom.support.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.support.model.SupportTicket;
import com.homebase.ecom.support.model.TicketMessage;
import com.homebase.ecom.support.dto.ReplyPayload;

public class ReplySupportAction extends AbstractSTMTransitionAction<SupportTicket, ReplyPayload> {

    @Override
    public void transitionTo(SupportTicket ticket,
            ReplyPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (payload.getMessage() == null || payload.getMessage().trim().isEmpty()) {
            throw new IllegalArgumentException("Reply message cannot be empty");
        }

        TicketMessage message = new TicketMessage();
        message.setId(java.util.UUID.randomUUID().toString());
        // Use sender type from payload if available, else default to AGENT
        String senderType = payload.getSenderType();
        if (senderType == null || senderType.trim().isEmpty()) {
            senderType = (String) ticket.getTransientMap().get("senderType");
        }
        message.setSenderType(senderType != null ? senderType : "AGENT");
        message.setSenderId(ticket.getAssignedTo());
        message.setMessage(payload.getMessage());
        ticket.getMessages().add(message);

        // Track last reply timestamp in transient map
        ticket.getTransientMap().put("lastReplyAt", new java.util.Date());

        ticket.getTransientMap().previousPayload = payload;
    }
}
