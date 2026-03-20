package com.homebase.ecom.support.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.support.model.SupportTicket;
import com.homebase.ecom.support.model.TicketMessage;
import com.homebase.ecom.support.dto.ReopenTicketPayload;
import com.homebase.ecom.support.service.validator.SupportPolicyValidator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.UUID;

public class ReopenSupportAction extends AbstractSTMTransitionAction<SupportTicket, ReopenTicketPayload> {

    @Autowired
    private SupportPolicyValidator policyValidator;

    @Override
    public void transitionTo(SupportTicket ticket,
            ReopenTicketPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (payload.getReason() == null || payload.getReason().trim().isEmpty()) {
            throw new IllegalArgumentException("Reason for reopening is required");
        }

        // Validate reopen limit
        policyValidator.validateReopen(ticket);

        // Increment reopen count
        ticket.setReopenCount(ticket.getReopenCount() + 1);

        // Clear resolved data
        ticket.setResolvedAt(null);
        // Clear assignment so ticket can be reassigned
        ticket.setAssignedAgentId(null);

        // Add system message about reopening
        TicketMessage reopenMsg = new TicketMessage();
        reopenMsg.setId(UUID.randomUUID().toString());
        reopenMsg.setSenderId("SYSTEM");
        reopenMsg.setSenderType("SYSTEM");
        reopenMsg.setTimestamp(new Date());
        reopenMsg.setMessage("Ticket reopened (reopen #" + ticket.getReopenCount() + "). Reason: " + payload.getReason());
        ticket.getMessages().add(reopenMsg);

        ticket.getTransientMap().previousPayload = payload;
    }
}
