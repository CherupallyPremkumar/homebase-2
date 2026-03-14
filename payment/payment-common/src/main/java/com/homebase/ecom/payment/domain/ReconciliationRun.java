package com.homebase.ecom.payment.domain;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents one periodic reconciliation execution between the internal ledger and gateway.
 * Extends BaseJpaEntity for audit fields (id, createdTime, lastModifiedTime, tenant, etc.)
 */
@Entity
@Table(name = "reconciliation_runs", indexes = {
        @Index(name = "idx_reconciliation_runs_gateway_period", columnList = "gateway_type,period_start,period_end")
})
public class ReconciliationRun extends BaseJpaEntity {

    @Column(name = "gateway_type", nullable = false, length = 20)
    private String gatewayType;

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReconciliationRunStatus status = ReconciliationRunStatus.STARTED;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String summary;

    @PrePersist
    void initStartedAt() {
        if (startedAt == null) {
            startedAt = LocalDateTime.now();
        }
    }

    public String getGatewayType() {
        return gatewayType;
    }

    public void setGatewayType(String gatewayType) {
        this.gatewayType = gatewayType;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
    }

    public ReconciliationRunStatus getStatus() {
        return status;
    }

    public void setStatus(ReconciliationRunStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
