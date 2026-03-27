package com.homebase.ecom.fulfillment.infrastructure.integration;

import com.homebase.ecom.fulfillment.model.FulfillmentSaga;
import com.homebase.ecom.fulfillment.port.FulfillmentEventPublisherPort;
import com.homebase.ecom.shared.event.KafkaTopics;
import org.chenile.pubsub.ChenilePub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Infrastructure adapter for publishing fulfillment domain events to Kafka.
 * Uses ChenilePub for event delivery to fulfillment.events topic.
 * No @Component -- wired explicitly via @Bean in FulfillmentConfiguration.
 */
public class KafkaFulfillmentEventPublisher implements FulfillmentEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaFulfillmentEventPublisher.class);

    private final ChenilePub chenilePub;
    private final ObjectMapper objectMapper;

    public KafkaFulfillmentEventPublisher(ChenilePub chenilePub, ObjectMapper objectMapper) {
        this.chenilePub = chenilePub;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publishInventoryReserved(FulfillmentSaga saga) {
        Map<String, Object> payload = basePayload(saga, "INVENTORY_RESERVED");
        publish(saga.getId(), payload, "INVENTORY_RESERVED");
    }

    @Override
    public void publishShipmentCreated(FulfillmentSaga saga) {
        Map<String, Object> payload = basePayload(saga, "SHIPMENT_CREATED");
        payload.put("shipmentId", nullSafe(saga.getShipmentId()));
        payload.put("trackingNumber", nullSafe(saga.getTrackingNumber()));
        publish(saga.getId(), payload, "SHIPMENT_CREATED");
    }

    @Override
    public void publishShipped(FulfillmentSaga saga) {
        Map<String, Object> payload = basePayload(saga, "SHIPPED");
        payload.put("shipmentId", nullSafe(saga.getShipmentId()));
        payload.put("trackingNumber", nullSafe(saga.getTrackingNumber()));
        publish(saga.getId(), payload, "SHIPPED");
    }

    @Override
    public void publishCustomerNotified(FulfillmentSaga saga) {
        Map<String, Object> payload = basePayload(saga, "CUSTOMER_NOTIFIED");
        publish(saga.getId(), payload, "CUSTOMER_NOTIFIED");
    }

    @Override
    public void publishCompleted(FulfillmentSaga saga) {
        Map<String, Object> payload = basePayload(saga, "FULFILLMENT_COMPLETED");
        payload.put("shipmentId", nullSafe(saga.getShipmentId()));
        payload.put("trackingNumber", nullSafe(saga.getTrackingNumber()));
        publish(saga.getId(), payload, "FULFILLMENT_COMPLETED");
    }

    @Override
    public void publishFailed(FulfillmentSaga saga, String previousState) {
        Map<String, Object> payload = basePayload(saga, "FULFILLMENT_FAILED");
        payload.put("errorMessage", nullSafe(saga.getErrorMessage()));
        payload.put("retryCount", saga.getRetryCount());
        payload.put("previousState", nullSafe(previousState));
        publish(saga.getId(), payload, "FULFILLMENT_FAILED");
    }

    @Override
    public void publishCompensationDone(FulfillmentSaga saga) {
        Map<String, Object> payload = basePayload(saga, "COMPENSATION_DONE");
        publish(saga.getId(), payload, "COMPENSATION_DONE");
    }

    private Map<String, Object> basePayload(FulfillmentSaga saga, String eventType) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("sagaId", nullSafe(saga.getId()));
        payload.put("orderId", nullSafe(saga.getOrderId()));
        payload.put("userId", nullSafe(saga.getUserId()));
        payload.put("eventType", eventType);
        return payload;
    }

    private void publish(String key, Map<String, Object> payload, String eventType) {
        try {
            String body = objectMapper.writeValueAsString(payload);
            chenilePub.publish(KafkaTopics.FULFILLMENT_EVENTS, body,
                    Map.of("key", key, "eventType", eventType));
            log.info("Published {} for saga {}", eventType, key);
        } catch (JacksonException e) {
            log.error("Failed to serialize {} for saga {}", eventType, key, e);
        }
    }

    private static String nullSafe(Object value) {
        return value != null ? value.toString() : "";
    }
}
