package com.homebase.ecom.support.domain.port;

import com.homebase.ecom.support.model.SupportTicket;

/**
 * Outbound port for publishing support ticket domain events.
 * Infrastructure layer provides Kafka implementation.
 * Domain/service layer depends only on this interface -- never on ChenilePub directly.
 */
public interface SupportEventPublisherPort {

    /**
     * Publishes event when a new support ticket is created.
     */
    void publishTicketCreated(SupportTicket ticket);

    /**
     * Publishes event when a support ticket is resolved.
     */
    void publishTicketResolved(SupportTicket ticket);

    /**
     * Publishes event when a support ticket is escalated.
     */
    void publishTicketEscalated(SupportTicket ticket);
}
