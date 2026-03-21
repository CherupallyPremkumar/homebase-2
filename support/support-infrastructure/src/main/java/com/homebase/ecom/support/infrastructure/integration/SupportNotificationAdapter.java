package com.homebase.ecom.support.infrastructure.integration;

import com.homebase.ecom.support.domain.port.NotificationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Infrastructure adapter for support notification operations.
 *
 * Currently a logging stub — will delegate to notification-client
 * once cross-service event wiring is complete.
 */
public class SupportNotificationAdapter implements NotificationPort {

    private static final Logger log = LoggerFactory.getLogger(SupportNotificationAdapter.class);

    @Override
    public void notifyAgentAssignment(String agentId, String ticketId, String subject, String priority) {
        log.info("Notification: agent assigned agentId={}, ticketId={}, subject={}, priority={}",
                agentId, ticketId, subject, priority);
    }

    @Override
    public void notifySupervisorEscalation(String ticketId, String subject, String priority,
                                           String escalationReason) {
        log.info("Notification: ticket escalated ticketId={}, subject={}, priority={}, reason={}",
                ticketId, subject, priority, escalationReason);
    }

    @Override
    public void notifyCustomerResolution(String customerId, String ticketId, String subject,
                                         String resolution) {
        log.info("Notification: ticket resolved customerId={}, ticketId={}, subject={}",
                customerId, ticketId, subject);
    }

    @Override
    public void sendSatisfactionSurvey(String customerId, String ticketId) {
        log.info("Notification: sending satisfaction survey customerId={}, ticketId={}",
                customerId, ticketId);
    }
}
