package com.homebase.ecom.support.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.support.model.SupportTicket;
import com.homebase.ecom.support.model.TicketMessage;
import com.homebase.ecom.support.dto.EscalatePayload;

/**
 * Escalates a support ticket from ASSIGNED to ESCALATED.
 */
public class EscalateSupportAction extends AbstractSTMTransitionAction<SupportTicket, EscalatePayload> {

    @Override
    public void transitionTo(SupportTicket ticket,
            EscalatePayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (payload.getEscalationReason() == null || payload.getEscalationReason().trim().isEmpty()) {
            throw new IllegalArgumentException("Escalation reason is required");
        }

        // Increase priority on escalation
        String currentPriority = ticket.getPriority();
        if ("LOW".equals(currentPriority)) {
            ticket.setPriority("MEDIUM");
        } else if ("MEDIUM".equals(currentPriority)) {
            ticket.setPriority("HIGH");
        } else if ("HIGH".equals(currentPriority)) {
            ticket.setPriority("URGENT");
        }
        // URGENT stays URGENT

        // Reassign to senior agent if specified
        if (payload.getEscalateTo() != null && !payload.getEscalateTo().trim().isEmpty()) {
            ticket.setAssignedTo(payload.getEscalateTo());
        } else {
            // Clear assignment so admin can reassign
            ticket.setAssignedTo(null);
        }

        // Add system message about escalation
        TicketMessage escalationMsg = new TicketMessage();
        escalationMsg.setId(java.util.UUID.randomUUID().toString());
        escalationMsg.setSenderType("SYSTEM");
        escalationMsg.setMessage("Ticket escalated. Reason: " + payload.getEscalationReason()
                + ". Priority changed to: " + ticket.getPriority());
        ticket.getMessages().add(escalationMsg);

        ticket.getTransientMap().previousPayload = payload;
    }
}
