package com.homebase.ecom.support.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.support.model.SupportTicket;
import com.homebase.ecom.support.model.TicketMessage;
import com.homebase.ecom.support.dto.AssignAgentPayload;
import com.homebase.ecom.support.service.validator.SupportPolicyValidator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.UUID;

public class AssignAgentSupportAction extends AbstractSTMTransitionAction<SupportTicket, AssignAgentPayload> {

    @Autowired
    private SupportPolicyValidator policyValidator;

    @Override
    public void transitionTo(SupportTicket ticket,
            AssignAgentPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (payload.getAgentId() == null || payload.getAgentId().trim().isEmpty()) {
            throw new IllegalArgumentException("Agent ID is required for assignment");
        }

        ticket.setAssignedAgentId(payload.getAgentId());

        // Set SLA deadline based on configured response hours and priority
        int slaHours = policyValidator.getSlaResponseHours();
        long slaMultiplier = 1L;
        if ("HIGH".equals(ticket.getPriority()) || "URGENT".equals(ticket.getPriority())) {
            slaMultiplier = 1L;
        } else if ("LOW".equals(ticket.getPriority())) {
            slaMultiplier = 4L;
        } else {
            slaMultiplier = 2L;
        }
        long slaDeadline = System.currentTimeMillis() + (slaHours * 60 * 60 * 1000L / slaMultiplier);
        ticket.getTransientMap().put("slaDeadline", slaDeadline);

        // Add system message about assignment
        TicketMessage assignMsg = new TicketMessage();
        assignMsg.setId(UUID.randomUUID().toString());
        assignMsg.setSenderId("SYSTEM");
        assignMsg.setSenderType("SYSTEM");
        assignMsg.setTimestamp(new Date());
        assignMsg.setMessage("Ticket assigned to agent: " + payload.getAgentId());
        ticket.getMessages().add(assignMsg);

        ticket.getTransientMap().previousPayload = payload;
    }
}
