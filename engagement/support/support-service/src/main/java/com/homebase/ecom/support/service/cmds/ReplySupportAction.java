package com.homebase.ecom.support.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.support.model.SupportTicket;
import com.homebase.ecom.support.model.TicketMessage;
import com.homebase.ecom.support.dto.ReplyPayload;

import java.util.Date;
import java.util.UUID;

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
        message.setId(UUID.randomUUID().toString());

        String senderType = payload.getSenderType();
        if (senderType == null || senderType.trim().isEmpty()) {
            senderType = (String) ticket.getTransientMap().get("senderType");
        }
        message.setSenderType(senderType != null ? senderType : "AGENT");
        String senderId = ticket.getAssignedAgentId();
        message.setSenderId(senderId != null ? senderId : "UNKNOWN");
        message.setMessage(payload.getMessage());
        message.setTimestamp(new Date());
        ticket.getMessages().add(message);

        ticket.getTransientMap().put("lastReplyAt", new Date());
        ticket.getTransientMap().previousPayload = payload;
    }
}
