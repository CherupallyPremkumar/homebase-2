package com.homebase.ecom.settlement.infrastructure.event;

import com.homebase.ecom.settlement.domain.port.SettlementEventPublisherPort;
import com.homebase.ecom.settlement.model.Settlement;
import com.homebase.ecom.shared.event.KafkaTopics;
import org.chenile.pubsub.ChenilePub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Kafka-backed implementation of SettlementEventPublisherPort.
 * Translates domain method calls into Kafka messages via ChenilePub.
 * No @Component -- wired explicitly via @Bean.
 */
public class KafkaSettlementEventPublisher implements SettlementEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaSettlementEventPublisher.class);

    private final ChenilePub chenilePub;
    private final ObjectMapper objectMapper;

    public KafkaSettlementEventPublisher(ChenilePub chenilePub, ObjectMapper objectMapper) {
        this.chenilePub = chenilePub;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publishSettlementApproved(Settlement settlement, String fromState) {
        publishStateChange(settlement, fromState, "APPROVED",
                "SETTLEMENT_CALCULATED",
                "Settlement calculated and approved. Net: " + settlement.getNetAmount());
    }

    @Override
    public void publishSettlementDisputed(Settlement settlement, String fromState) {
        publishStateChange(settlement, fromState, "DISPUTED",
                "SETTLEMENT_DISPUTED",
                "Settlement disputed by supplier");
    }

    @Override
    public void publishSettlementDisbursed(Settlement settlement, String fromState) {
        publishStateChange(settlement, fromState, "DISBURSED",
                "SETTLEMENT_DISBURSED",
                "Settlement disbursed. Ref: " + settlement.getDisbursementReference()
                        + ", Amount: " + settlement.getNetAmount());
    }

    private void publishStateChange(Settlement settlement, String fromState,
                                     String toState, String eventType, String description) {
        try {
            SettlementStateChangePayload payload = new SettlementStateChangePayload(
                    settlement.getId(), settlement.getSupplierId(),
                    fromState, toState, description, LocalDateTime.now());
            String body = objectMapper.writeValueAsString(payload);
            chenilePub.publish(KafkaTopics.SETTLEMENT_EVENTS, body,
                    Map.of("key", settlement.getId(), "eventType", eventType));
            log.info("Published {} for settlement {}", eventType, settlement.getId());
        } catch (JacksonException e) {
            log.error("Failed to publish {} event for settlement {}", eventType, settlement.getId(), e);
        }
    }

    /**
     * Internal Kafka message payload for settlement state changes.
     */
    private record SettlementStateChangePayload(
            String settlementId,
            String supplierId,
            String fromState,
            String toState,
            String description,
            LocalDateTime timestamp) {
    }
}
