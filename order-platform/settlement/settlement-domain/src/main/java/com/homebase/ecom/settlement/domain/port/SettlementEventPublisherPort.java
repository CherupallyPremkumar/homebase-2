package com.homebase.ecom.settlement.domain.port;

import com.homebase.ecom.settlement.model.Settlement;

/**
 * Outbound port for publishing settlement domain events.
 * Infrastructure layer provides the Kafka-backed implementation.
 * No @Component -- wired explicitly via @Bean.
 */
public interface SettlementEventPublisherPort {

    /**
     * Publish settlement calculated/approved event.
     *
     * @param settlement the approved settlement
     * @param fromState  the state transitioned from (may be null)
     */
    void publishSettlementApproved(Settlement settlement, String fromState);

    /**
     * Publish settlement disputed event.
     *
     * @param settlement the disputed settlement
     * @param fromState  the state transitioned from (may be null)
     */
    void publishSettlementDisputed(Settlement settlement, String fromState);

    /**
     * Publish settlement disbursed event.
     *
     * @param settlement the disbursed settlement
     * @param fromState  the state transitioned from (may be null)
     */
    void publishSettlementDisbursed(Settlement settlement, String fromState);
}
