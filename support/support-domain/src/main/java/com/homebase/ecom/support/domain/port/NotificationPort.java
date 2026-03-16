package com.homebase.ecom.support.domain.port;

/**
 * Hexagonal port for sending notifications from the support module.
 * Infrastructure adapter can integrate with email, SMS, push notifications.
 */
public interface NotificationPort {

    /**
     * Notify an agent about a new ticket assignment.
     */
    void notifyAgentAssignment(String agentId, String ticketId, String subject, String priority);

    /**
     * Notify a supervisor about a ticket escalation.
     */
    void notifySupervisorEscalation(String ticketId, String subject, String priority, String escalationReason);

    /**
     * Notify a customer about ticket resolution.
     */
    void notifyCustomerResolution(String customerId, String ticketId, String subject, String resolution);

    /**
     * Send a customer satisfaction survey after ticket resolution.
     */
    void sendSatisfactionSurvey(String customerId, String ticketId);
}
