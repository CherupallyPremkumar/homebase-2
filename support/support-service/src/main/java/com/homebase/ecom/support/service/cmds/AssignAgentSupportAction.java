package com.homebase.ecom.support.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.support.model.SupportTicket;
import com.homebase.ecom.support.model.TicketMessage;
import com.homebase.ecom.support.dto.AssignAgentPayload;

public class AssignAgentSupportAction extends AbstractSTMTransitionAction<SupportTicket, AssignAgentPayload> {

    private static final long SLA_RESPONSE_TIME_MS = 4 * 60 * 60 * 1000L; // 4 hours

    @Override
    public void transitionTo(SupportTicket ticket,
            AssignAgentPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (payload.getAgentId() == null || payload.getAgentId().trim().isEmpty()) {
            throw new IllegalArgumentException("Agent ID is required for assignment");
        }

        ticket.setAssignedTo(payload.getAgentId());

        // Set SLA timer based on priority
        long slaMultiplier = 1L;
        if ("HIGH".equals(ticket.getPriority()) || "URGENT".equals(ticket.getPriority())) {
            slaMultiplier = 1L; // 4 hours for high/urgent
        } else if ("LOW".equals(ticket.getPriority())) {
            slaMultiplier = 4L; // 16 hours for low
        } else {
            slaMultiplier = 2L; // 8 hours for medium (default)
        }
        long slaDeadline = System.currentTimeMillis() + (SLA_RESPONSE_TIME_MS * slaMultiplier);
        ticket.getTransientMap().put("slaDeadline", slaDeadline);

        // Add system message about assignment
        TicketMessage assignMsg = new TicketMessage();
        assignMsg.setSenderType("SYSTEM");
        assignMsg.setMessage("Ticket assigned to agent: " + payload.getAgentId());
        ticket.getMessages().add(assignMsg);

        ticket.getTransientMap().previousPayload = payload;
    }
}
