package com.homebase.ecom.settlement.service.postSaveHooks;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Spring application event published when a settlement changes state.
 * Consumers of this event can forward to Kafka, send notifications, etc.
 */
public class SettlementStateChangeEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String settlementId;
    private final String supplierId;
    private final String fromState;
    private final String toState;
    private final String description;
    private final LocalDateTime timestamp;

    public SettlementStateChangeEvent(String settlementId, String supplierId,
                                       String fromState, String toState, String description) {
        this.settlementId = settlementId;
        this.supplierId = supplierId;
        this.fromState = fromState;
        this.toState = toState;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    public String getSettlementId() { return settlementId; }
    public String getSupplierId() { return supplierId; }
    public String getFromState() { return fromState; }
    public String getToState() { return toState; }
    public String getDescription() { return description; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "SettlementStateChangeEvent{" +
                "settlementId='" + settlementId + '\'' +
                ", supplierId='" + supplierId + '\'' +
                ", fromState='" + fromState + '\'' +
                ", toState='" + toState + '\'' +
                ", description='" + description + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
