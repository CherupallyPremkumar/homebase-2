package com.homebase.ecom.returnprocessing.infrastructure.integration;

import com.homebase.ecom.returnprocessing.model.ReturnProcessingSaga;
import com.homebase.ecom.returnprocessing.port.ReturnProcessingEventPublisherPort;
import com.homebase.ecom.shared.event.KafkaTopics;
import org.chenile.pubsub.ChenilePub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Infrastructure adapter for publishing return processing domain events to Kafka.
 * Uses ChenilePub for event delivery to return-processing.events topic.
 * No @Component -- wired explicitly via @Bean in ReturnProcessingInfrastructureConfiguration.
 */
public class KafkaReturnProcessingEventPublisher implements ReturnProcessingEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaReturnProcessingEventPublisher.class);

    private final ChenilePub chenilePub;
    private final ObjectMapper objectMapper;

    public KafkaReturnProcessingEventPublisher(ChenilePub chenilePub, ObjectMapper objectMapper) {
        this.chenilePub = chenilePub;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publishPickupScheduled(ReturnProcessingSaga saga) {
        Map<String, Object> payload = basePayload(saga, "PICKUP_SCHEDULED");
        payload.put("shipmentId", nullSafe(saga.getShipmentId()));
        publish(saga.getId(), payload, "PICKUP_SCHEDULED");
    }

    @Override
    public void publishItemReceived(ReturnProcessingSaga saga) {
        Map<String, Object> payload = basePayload(saga, "ITEM_RECEIVED");
        payload.put("orderItemId", nullSafe(saga.getOrderItemId()));
        publish(saga.getId(), payload, "ITEM_RECEIVED");
    }

    @Override
    public void publishInventoryRestocked(ReturnProcessingSaga saga) {
        Map<String, Object> payload = basePayload(saga, "INVENTORY_RESTOCKED");
        payload.put("orderItemId", nullSafe(saga.getOrderItemId()));
        publish(saga.getId(), payload, "INVENTORY_RESTOCKED");
    }

    @Override
    public void publishSettlementAdjusted(ReturnProcessingSaga saga) {
        Map<String, Object> payload = basePayload(saga, "SETTLEMENT_ADJUSTED");
        payload.put("settlementAdjustmentId", nullSafe(saga.getSettlementAdjustmentId()));
        payload.put("refundAmount", nullSafe(saga.getRefundAmount()));
        publish(saga.getId(), payload, "SETTLEMENT_ADJUSTED");
    }

    @Override
    public void publishRefundProcessed(ReturnProcessingSaga saga) {
        Map<String, Object> payload = basePayload(saga, "REFUND_PROCESSED");
        payload.put("refundId", nullSafe(saga.getRefundId()));
        payload.put("refundAmount", nullSafe(saga.getRefundAmount()));
        publish(saga.getId(), payload, "REFUND_PROCESSED");
    }

    @Override
    public void publishReturnProcessingCompleted(ReturnProcessingSaga saga) {
        Map<String, Object> payload = basePayload(saga, "RETURN_PROCESSING_COMPLETED");
        payload.put("orderItemId", nullSafe(saga.getOrderItemId()));
        payload.put("refundId", nullSafe(saga.getRefundId()));
        payload.put("refundAmount", nullSafe(saga.getRefundAmount()));
        payload.put("settlementAdjustmentId", nullSafe(saga.getSettlementAdjustmentId()));
        publish(saga.getId(), payload, "RETURN_PROCESSING_COMPLETED");
    }

    @Override
    public void publishReturnProcessingFailed(ReturnProcessingSaga saga, String previousState) {
        Map<String, Object> payload = basePayload(saga, "RETURN_PROCESSING_FAILED");
        payload.put("errorMessage", nullSafe(saga.getErrorMessage()));
        payload.put("retryCount", saga.getRetryCount());
        payload.put("previousState", nullSafe(previousState));
        publish(saga.getId(), payload, "RETURN_PROCESSING_FAILED");
    }

    @Override
    public void requestPickupScheduling(ReturnProcessingSaga saga) {
        Map<String, Object> payload = basePayload(saga, "PICKUP_SCHEDULING_REQUESTED");
        payload.put("orderItemId", nullSafe(saga.getOrderItemId()));
        publish(saga.getId(), payload, "PICKUP_SCHEDULING_REQUESTED");
    }

    @Override
    public void requestInventoryRestock(ReturnProcessingSaga saga) {
        Map<String, Object> payload = basePayload(saga, "INVENTORY_RESTOCK_REQUESTED");
        payload.put("orderItemId", nullSafe(saga.getOrderItemId()));
        publish(saga.getId(), payload, "INVENTORY_RESTOCK_REQUESTED");
    }

    @Override
    public void requestSettlementAdjustment(ReturnProcessingSaga saga) {
        Map<String, Object> payload = basePayload(saga, "SETTLEMENT_ADJUSTMENT_REQUESTED");
        payload.put("refundAmount", nullSafe(saga.getRefundAmount()));
        publish(saga.getId(), payload, "SETTLEMENT_ADJUSTMENT_REQUESTED");
    }

    @Override
    public void requestRefundProcessing(ReturnProcessingSaga saga) {
        Map<String, Object> payload = basePayload(saga, "REFUND_PROCESSING_REQUESTED");
        payload.put("refundAmount", nullSafe(saga.getRefundAmount()));
        payload.put("settlementAdjustmentId", nullSafe(saga.getSettlementAdjustmentId()));
        publish(saga.getId(), payload, "REFUND_PROCESSING_REQUESTED");
    }

    private Map<String, Object> basePayload(ReturnProcessingSaga saga, String eventType) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("sagaId", nullSafe(saga.getId()));
        payload.put("returnRequestId", nullSafe(saga.getReturnRequestId()));
        payload.put("orderId", nullSafe(saga.getOrderId()));
        payload.put("eventType", eventType);
        return payload;
    }

    private void publish(String key, Map<String, Object> payload, String eventType) {
        try {
            String body = objectMapper.writeValueAsString(payload);
            chenilePub.publish(KafkaTopics.RETURN_PROCESSING_EVENTS, body,
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
