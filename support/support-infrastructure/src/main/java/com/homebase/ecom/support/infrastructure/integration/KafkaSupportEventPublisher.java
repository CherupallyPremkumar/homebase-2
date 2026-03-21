package com.homebase.ecom.support.infrastructure.integration;

import com.homebase.ecom.support.domain.port.SupportEventPublisherPort;
import com.homebase.ecom.support.model.SupportTicket;
import com.homebase.ecom.shared.event.KafkaTopics;
import org.chenile.pubsub.ChenilePub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * Infrastructure adapter for publishing support ticket domain events to Kafka.
 * Uses ChenilePub for event delivery to support.events topic.
 * No @Component -- wired explicitly via @Bean in SupportInfrastructureConfiguration.
 */
public class KafkaSupportEventPublisher implements SupportEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaSupportEventPublisher.class);

    private final ChenilePub chenilePub;
    private final ObjectMapper objectMapper;

    public KafkaSupportEventPublisher(ChenilePub chenilePub, ObjectMapper objectMapper) {
        this.chenilePub = chenilePub;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publishTicketCreated(SupportTicket ticket) {
        publish(ticket, "TICKET_CREATED");
    }

    @Override
    public void publishTicketResolved(SupportTicket ticket) {
        publish(ticket, "TICKET_RESOLVED");
    }

    @Override
    public void publishTicketEscalated(SupportTicket ticket) {
        publish(ticket, "TICKET_ESCALATED");
    }

    private void publish(SupportTicket ticket, String eventType) {
        try {
            Map<String, Object> eventPayload = Map.of(
                    "ticketId", nullSafe(ticket.getId()),
                    "customerId", nullSafe(ticket.getCustomerId()),
                    "category", nullSafe(ticket.getCategory()),
                    "priority", nullSafe(ticket.getPriority()),
                    "subject", nullSafe(ticket.getSubject()),
                    "assignedAgentId", nullSafe(ticket.getAssignedAgentId()),
                    "eventType", eventType
            );
            String body = objectMapper.writeValueAsString(eventPayload);
            chenilePub.publish(KafkaTopics.SUPPORT_EVENTS, body,
                    Map.of("key", ticket.getId(), "eventType", eventType));
            log.info("Published {} for ticket {}", eventType, ticket.getId());
        } catch (JacksonException e) {
            log.error("Failed to serialize {} for ticket {}", eventType, ticket.getId(), e);
        }
    }

    private static String nullSafe(Object value) {
        return value != null ? value.toString() : "";
    }
}
