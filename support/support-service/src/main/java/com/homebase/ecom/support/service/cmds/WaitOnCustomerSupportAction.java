package com.homebase.ecom.support.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.support.model.SupportTicket;
import com.homebase.ecom.support.model.TicketMessage;
import com.homebase.ecom.support.dto.WaitOnCustomerPayload;

import java.util.Date;
import java.util.UUID;

/**
 * Transition action for IN_PROGRESS -> WAITING_ON_CUSTOMER.
 * Agent puts the ticket on hold while waiting for customer input.
 */
public class WaitOnCustomerSupportAction extends AbstractSTMTransitionAction<SupportTicket, WaitOnCustomerPayload> {

    @Override
    public void transitionTo(SupportTicket ticket,
            WaitOnCustomerPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        String reason = payload.getWaitReason();
        if (reason == null || reason.trim().isEmpty()) {
            reason = "Waiting for customer response";
        }

        TicketMessage msg = new TicketMessage();
        msg.setId(UUID.randomUUID().toString());
        msg.setSenderId("SYSTEM");
        msg.setSenderType("SYSTEM");
        msg.setTimestamp(new Date());
        msg.setMessage("Ticket placed on hold: " + reason);
        ticket.getMessages().add(msg);

        ticket.getTransientMap().previousPayload = payload;
    }
}
