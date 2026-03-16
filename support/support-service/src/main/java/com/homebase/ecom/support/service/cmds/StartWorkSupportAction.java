package com.homebase.ecom.support.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.support.model.SupportTicket;
import com.homebase.ecom.support.model.TicketMessage;
import com.homebase.ecom.support.dto.StartWorkPayload;

import java.util.Date;
import java.util.UUID;

/**
 * Transition action for ASSIGNED -> IN_PROGRESS.
 * Marks the ticket as actively being worked on.
 */
public class StartWorkSupportAction extends AbstractSTMTransitionAction<SupportTicket, StartWorkPayload> {

    @Override
    public void transitionTo(SupportTicket ticket,
            StartWorkPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        TicketMessage msg = new TicketMessage();
        msg.setId(UUID.randomUUID().toString());
        msg.setSenderType("SYSTEM");
        msg.setTimestamp(new Date());
        msg.setMessage("Agent " + ticket.getAssignedAgentId() + " started working on this ticket.");
        ticket.getMessages().add(msg);

        ticket.getTransientMap().previousPayload = payload;
    }
}
