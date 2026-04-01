package com.homebase.ecom.support.service.event;

import com.homebase.ecom.support.model.SupportTicket;
import com.homebase.ecom.shared.event.EventEnvelope;
import com.homebase.ecom.shared.event.KafkaTopics;
import org.chenile.pubsub.ChenilePub;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * Chenile event handler for support cross-service events.
 * Registered via supportEventService.json — operations subscribe to Kafka topics
 * through chenile-kafka auto-subscription.
 *
 * Consumes:
 * - order.events: ORDER_CANCELLED with reason=ISSUE -> auto-create support ticket
 * - return.events: RETURN_REJECTED -> auto-create support ticket
 *
 * Bean name "supportEventService" must match the service JSON id.
 */
public class SupportEventHandler {

    private static final Logger log = LoggerFactory.getLogger(SupportEventHandler.class);

    private final StateEntityServiceImpl<SupportTicket> supportStateEntityService;
    private final ChenilePub chenilePub;
    private final ObjectMapper objectMapper;

    public SupportEventHandler(
            @Qualifier("_supportStateEntityService_") StateEntityServiceImpl<SupportTicket> supportStateEntityService,
            ChenilePub chenilePub,
            ObjectMapper objectMapper) {
        this.supportStateEntityService = supportStateEntityService;
        this.chenilePub = chenilePub;
        this.objectMapper = objectMapper;
    }

    // ── order.events ──────────────────────────────────────────────────────

    /**
     * Handles order events. When an ORDER_CANCELLED event arrives with
     * reason containing "ISSUE", auto-creates a support ticket.
     */
    @Transactional
    @SuppressWarnings("unchecked")
    public void handleOrderEvent(EventEnvelope envelope) {
        if (envelope == null || envelope.getEventType() == null) {
            return;
        }

        if (!"ORDER_CANCELLED".equals(envelope.getEventType())) {
            return;
        }

        try {
            Map<String, Object> payload = objectMapper.convertValue(envelope.getPayload(), Map.class);
            String orderId = (String) payload.get("orderId");
            String customerId = (String) payload.get("customerId");
            String reason = payload.get("reason") != null ? payload.get("reason").toString() : "";

            // Only auto-create ticket if cancellation reason indicates an issue
            if (!reason.toUpperCase().contains("ISSUE")) {
                log.debug("Order {} cancelled but reason '{}' does not indicate an issue. Skipping ticket creation.",
                        orderId, reason);
                return;
            }

            log.info("Support: received ORDER_CANCELLED with ISSUE for order: {}. Creating support ticket.", orderId);

            SupportTicket ticket = new SupportTicket();
            ticket.setCustomerId(customerId);
            ticket.setOrderId(orderId);
            ticket.setCategory("ORDER");
            ticket.setPriority("HIGH");
            ticket.setSubject("Order Cancelled Due to Issue - " + orderId);
            ticket.setDescription("Auto-created ticket: Order " + orderId + " was cancelled. Reason: " + reason);

            // Create through STM — sets initial state (OPEN) and persists via EntityStore
            supportStateEntityService.process(ticket, null, null);
            log.info("Auto-created support ticket for cancelled order: {}", orderId);

        } catch (RuntimeException e) {
            log.warn("Idempotency: support ticket already created for order event (possible replay). Skipping. Detail: {}",
                    e.getMessage());
        } catch (Exception e) {
            log.error("Error processing order event for support ticket creation", e);
        }
    }

    // ── return.events ─────────────────────────────────────────────────────

    /**
     * Handles return events. When a RETURN_REJECTED event arrives,
     * auto-creates a support ticket for the customer.
     */
    @Transactional
    @SuppressWarnings("unchecked")
    public void handleReturnEvent(EventEnvelope envelope) {
        if (envelope == null || envelope.getEventType() == null) {
            return;
        }

        if (!"RETURN_REJECTED".equals(envelope.getEventType())) {
            return;
        }

        try {
            Map<String, Object> payload = objectMapper.convertValue(envelope.getPayload(), Map.class);
            String orderId = (String) payload.get("orderId");
            String customerId = (String) payload.get("customerId");
            String reason = payload.get("reason") != null ? payload.get("reason").toString() : "No reason provided";

            log.info("Support: received RETURN_REJECTED for order: {}. Creating support ticket.", orderId);

            SupportTicket ticket = new SupportTicket();
            ticket.setCustomerId(customerId);
            ticket.setOrderId(orderId);
            ticket.setCategory("ORDER");
            ticket.setPriority("MEDIUM");
            ticket.setSubject("Return Rejected - " + orderId);
            ticket.setDescription("Auto-created ticket: Return for order " + orderId + " was rejected. Reason: " + reason);

            // Create through STM
            supportStateEntityService.process(ticket, null, null);
            log.info("Auto-created support ticket for rejected return: {}", orderId);

        } catch (RuntimeException e) {
            log.warn("Idempotency: support ticket already created for return event (possible replay). Skipping. Detail: {}",
                    e.getMessage());
        } catch (Exception e) {
            log.error("Error processing return event for support ticket creation", e);
        }
    }
}
