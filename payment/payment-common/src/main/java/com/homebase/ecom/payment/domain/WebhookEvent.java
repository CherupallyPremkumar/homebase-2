package com.homebase.ecom.payment.domain;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

/**
 * Idempotent record of every gateway webhook received.
 * Extends BaseJpaEntity for audit fields (id, createdTime, lastModifiedTime, tenant, etc.)
 */
@Entity
@Table(name = "webhook_events", indexes = {
        @Index(name = "idx_webhook_events_type_processed", columnList = "event_type,processed")
})
public class WebhookEvent extends BaseJpaEntity {

    @Column(name = "gateway_event_id", unique = true, nullable = false)
    private String gatewayEventId;

    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private String payload;

    @Column(nullable = false)
    private boolean processed = false;

    @Column(name = "processed_at")
    private java.time.LocalDateTime processedAt;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    // --- Getters & Setters ---

    public String getGatewayEventId() {
        return gatewayEventId;
    }

    public void setGatewayEventId(String gatewayEventId) {
        this.gatewayEventId = gatewayEventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public java.time.LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(java.time.LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
