package com.homebase.ecom.payment.infrastructure.persistence.entity;

import com.homebase.ecom.payment.infrastructure.enums.ReconciliationCategory;
import com.homebase.ecom.payment.infrastructure.enums.ReconciliationItemStatus;
import com.homebase.ecom.payment.infrastructure.enums.ReconciliationSeverity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A single line item in a reconciliation run - represents one compared transaction.
 * Extends BaseJpaEntity for audit fields (id, createdTime, lastModifiedTime, tenant, etc.)
 */
@Entity
@Table(name = "reconciliation_items", indexes = {
        @Index(name = "idx_reconciliation_items_run_status", columnList = "run_id,status"),
        @Index(name = "idx_reconciliation_items_category", columnList = "category")
})
public class ReconciliationItem extends BaseJpaEntity {

    @Column(name = "run_id", nullable = false)
    private String runId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ReconciliationCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReconciliationSeverity severity;

    @Column(name = "provider_ref", length = 255)
    private String providerRef;

    @Column(name = "internal_ref", length = 255)
    private String internalRef;

    @Column(name = "expected_amount")
    private BigDecimal expectedAmount;

    @Column(name = "actual_amount")
    private BigDecimal actualAmount;

    @Column(length = 3, nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReconciliationItemStatus status = ReconciliationItemStatus.OPEN;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "resolved_by", length = 255)
    private String resolvedBy;

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public ReconciliationCategory getCategory() {
        return category;
    }

    public void setCategory(ReconciliationCategory category) {
        this.category = category;
    }

    public ReconciliationSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(ReconciliationSeverity severity) {
        this.severity = severity;
    }

    public String getProviderRef() {
        return providerRef;
    }

    public void setProviderRef(String providerRef) {
        this.providerRef = providerRef;
    }

    public String getInternalRef() {
        return internalRef;
    }

    public void setInternalRef(String internalRef) {
        this.internalRef = internalRef;
    }

    public BigDecimal getExpectedAmount() {
        return expectedAmount;
    }

    public void setExpectedAmount(BigDecimal expectedAmount) {
        this.expectedAmount = expectedAmount;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public ReconciliationItemStatus getStatus() {
        return status;
    }

    public void setStatus(ReconciliationItemStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public String getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(String resolvedBy) {
        this.resolvedBy = resolvedBy;
    }
}
