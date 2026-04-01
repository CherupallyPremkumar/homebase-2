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
 * Post Save Hook for the RESOLVED state -- TicketResolvedHook.
 * Notifies customer about resolution and sends satisfaction survey.
 * Publishes TICKET_RESOLVED via domain port.
 */
public class RESOLVEDSupportPostSaveHook implements PostSaveHook<SupportTicket> {

    private static final Logger log = LoggerFactory.getLogger(RESOLVEDSupportPostSaveHook.class);

    @Autowired
    private NotificationPort notificationPort;

    @Autowired
    private SupportEventPublisherPort supportEventPublisherPort;

    @Override
    public void execute(State startState, State endState, SupportTicket ticket, TransientMap map) {
        log.info("TICKET_RESOLVED: Ticket {} resolved at {} by agent {}",
                ticket.getId(), ticket.getResolvedAt(), ticket.getAssignedAgentId());

        // Notify customer about resolution
        if (notificationPort != null && ticket.getCustomerId() != null) {
            notificationPort.notifyCustomerResolution(
                    ticket.getCustomerId(), ticket.getId(),
                    ticket.getSubject(), ticket.getDescription());

            // Send satisfaction survey
            notificationPort.sendSatisfactionSurvey(ticket.getCustomerId(), ticket.getId());
        }

        // Publish TICKET_RESOLVED event via domain port
        if (supportEventPublisherPort != null) {
            supportEventPublisherPort.publishTicketResolved(ticket);
        }

        map.put("eventType", "TICKET_RESOLVED");
        map.put("resolvedAt", ticket.getResolvedAt());
    }
}
