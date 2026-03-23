package com.homebase.ecom.checkout.service.handler;

import com.homebase.ecom.core.workflow.HmStateEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

/**
 * Handles incoming cross-BC events and translates them into checkout STM transitions.
 *
 * Receives raw event JSON from payment.events topic and calls processById
 * on the checkout service for the affected checkout.
 *
 * Event mappings:
 *   PAYMENT_SUCCEEDED -> STM event "paymentSuccess"
 *   PAYMENT_FAILED    -> STM event "paymentFailed"
 *
 * No fan-out needed -- each payment event targets exactly one checkout.
 */
public class CheckoutExternalEventHandler {

    private static final Logger log = LoggerFactory.getLogger(CheckoutExternalEventHandler.class);

    private final ObjectMapper objectMapper;

    public CheckoutExternalEventHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void handle(String eventPayload, HmStateEntityServiceImpl<?> checkoutService) {
        try {
            JsonNode event = objectMapper.readTree(eventPayload);
            String eventType = event.has("eventType") ? event.get("eventType").asText() : null;

            if (eventType == null) {
                log.warn("Received event without eventType, skipping: {}", eventPayload);
                return;
            }

            switch (eventType) {
                case "PAYMENT_SUCCEEDED" -> handlePaymentSucceeded(event, checkoutService);
                case "PAYMENT_FAILED" -> handlePaymentFailed(event, checkoutService);
                default -> log.debug("Ignoring event type: {}", eventType);
            }
        } catch (Exception e) {
            log.error("Failed to handle external event: {}", eventPayload, e);
        }
    }

    private void handlePaymentSucceeded(JsonNode event, HmStateEntityServiceImpl<?> checkoutService) {
        String checkoutId = textOrNull(event, "checkoutId");
        if (checkoutId == null) {
            log.warn("PAYMENT_SUCCEEDED missing checkoutId, skipping");
            return;
        }
        log.info("Payment succeeded for checkoutId={}", checkoutId);
        triggerEvent(checkoutService, checkoutId, "paymentSuccess", event.toString());
    }

    private void handlePaymentFailed(JsonNode event, HmStateEntityServiceImpl<?> checkoutService) {
        String checkoutId = textOrNull(event, "checkoutId");
        if (checkoutId == null) {
            log.warn("PAYMENT_FAILED missing checkoutId, skipping");
            return;
        }
        log.info("Payment failed for checkoutId={}", checkoutId);
        triggerEvent(checkoutService, checkoutId, "paymentFailed", event.toString());
    }

    private void triggerEvent(HmStateEntityServiceImpl<?> checkoutService, String checkoutId,
                              String eventId, String payload) {
        try {
            checkoutService.processById(checkoutId, eventId, payload);
        } catch (Exception e) {
            log.error("Failed to trigger {} for checkoutId={}", eventId, checkoutId, e);
        }
    }

    private String textOrNull(JsonNode node, String field) {
        return node.has(field) && !node.get(field).isNull() ? node.get(field).asText() : null;
    }
}
