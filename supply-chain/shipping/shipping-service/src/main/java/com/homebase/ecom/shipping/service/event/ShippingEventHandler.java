package com.homebase.ecom.shipping.service.event;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.shared.event.*;
import com.homebase.ecom.shipping.model.Shipping;
import org.chenile.pubsub.ChenilePub;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

/**
 * Chenile event handler for shipping cross-service events.
 * Registered via shippingEventService.json — operations subscribe to Kafka topics
 * through chenile-kafka auto-subscription (CustomKafkaConsumer + EventProcessor).
 *
 * CONSUMES order.events:
 *   - ORDER_PAID -> create shipment (initializes through STM)
 *   - ORDER_CANCELLED -> cancel shipment
 *
 * Bean name "shippingEventService" must match the service JSON id.
 */
public class ShippingEventHandler {

    private static final Logger log = LoggerFactory.getLogger(ShippingEventHandler.class);

    private final StateEntityServiceImpl<Shipping> shippingStateEntityService;
    private final ChenilePub chenilePub;
    private final ObjectMapper objectMapper;

    public ShippingEventHandler(
            @Qualifier("_shippingStateEntityService_") StateEntityServiceImpl<Shipping> shippingStateEntityService,
            ChenilePub chenilePub,
            ObjectMapper objectMapper) {
        this.shippingStateEntityService = shippingStateEntityService;
        this.chenilePub = chenilePub;
        this.objectMapper = objectMapper;
    }

    // ── order.events ──────────────────────────────────────────────────────

    @Transactional
    public void handleOrderEvent(EventEnvelope envelope) {
        if (envelope == null || envelope.getEventType() == null) {
            return;
        }

        try {
            switch (envelope.getEventType()) {
                case "ORDER_PAID": {
                    handleOrderPaid(envelope);
                    break;
                }
                case "ORDER_CANCELLED": {
                    handleOrderCancelled(envelope);
                    break;
                }
                default:
                    // ignore other order event types
            }
        } catch (RuntimeException e) {
            log.warn("Idempotency: shipping state transition already applied for order event {} (possible replay). Skipping. Detail: {}",
                    envelope.getEventType(), e.getMessage());
        } catch (Exception e) {
            log.error("Error processing order event: {}", envelope.getEventType(), e);
        }
    }

    /**
     * ORDER_PAID -> create a new shipment in PENDING state via STM.
     */
    private void handleOrderPaid(EventEnvelope envelope) throws Exception {
        Map<String, Object> payload = (Map<String, Object>) envelope.getPayload();
        String orderId = (String) payload.get("orderId");
        String customerId = (String) payload.get("customerId");
        String toAddress = payload.get("shippingAddress") != null ? payload.get("shippingAddress").toString() : null;

        log.info("Shipping: received ORDER_PAID for order: {}", orderId);

        Shipping shipping = new Shipping();
        shipping.setId("SHIP-" + orderId);
        shipping.setOrderId(orderId);
        shipping.setCustomerId(customerId);
        shipping.setCarrier("HOMEBASE-LOGISTICS");
        shipping.setShippingMethod("STANDARD");
        if (toAddress != null) {
            shipping.setToAddress(toAddress);
        }

        // Initialize through STM -> moves to PENDING state
        shippingStateEntityService.process(shipping, null, null);
        log.info("Shipping record {} created for order {}.", shipping.getId(), orderId);
    }

    /**
     * ORDER_CANCELLED -> cancel shipment if still in PENDING or LABEL_CREATED state.
     */
    private void handleOrderCancelled(EventEnvelope envelope) throws Exception {
        Map<String, Object> payload = (Map<String, Object>) envelope.getPayload();
        String orderId = (String) payload.get("orderId");

        log.info("Shipping: received ORDER_CANCELLED for order: {}", orderId);

        String shippingId = "SHIP-" + orderId;
        try {
            shippingStateEntityService.processById(
                    shippingId, "cancelShipment",
                    new com.homebase.ecom.shipping.dto.CancelShipmentShippingPayload());
            log.info("Shipment {} cancelled for order {}.", shippingId, orderId);
        } catch (Exception e) {
            log.warn("Could not cancel shipment {} (may already be in transit): {}", shippingId, e.getMessage());
        }
    }
}
