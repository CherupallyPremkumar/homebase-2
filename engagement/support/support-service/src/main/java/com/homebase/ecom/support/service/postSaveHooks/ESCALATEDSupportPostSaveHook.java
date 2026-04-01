package com.homebase.ecom.support.service.postSaveHooks;

import com.homebase.ecom.support.domain.port.NotificationPort;
import com.homebase.ecom.support.domain.port.SupportEventPublisherPort;
import com.homebase.ecom.support.model.SupportTicket;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Post Save Hook for the ESCALATED state -- TicketEscalatedHook.
 * Notifies supervisor about escalation.
 * Publishes TICKET_ESCALATED via domain port.
 */
public class ESCALATEDSupportPostSaveHook implements PostSaveHook<SupportTicket> {

    private static final Logger log = LoggerFactory.getLogger(ESCALATEDSupportPostSaveHook.class);

    @Autowired
    private NotificationPort notificationPort;

    @Autowired
    private SupportEventPublisherPort supportEventPublisherPort;

    @Override
    public void execute(State startState, State endState, SupportTicket ticket, TransientMap map) {
        log.info("TICKET_ESCALATED: Ticket {} escalated from state {} to ESCALATED. Priority: {}",
                ticket.getId(),
                startState != null ? startState.getStateId() : "null",
                ticket.getPriority());

        // Notify supervisor
        if (notificationPort != null) {
            Object previousPayload = map.previousPayload;
            String escalationReason = previousPayload != null ? previousPayload.toString() : "Escalated";
            notificationPort.notifySupervisorEscalation(
                    ticket.getId(), ticket.getSubject(), ticket.getPriority(), escalationReason);
        }

        // Publish TICKET_ESCALATED event via domain port
        if (supportEventPublisherPort != null) {
            supportEventPublisherPort.publishTicketEscalated(ticket);
        }

        map.put("eventType", "TICKET_ESCALATED");
        map.put("escalatedPriority", ticket.getPriority());
    }
}
