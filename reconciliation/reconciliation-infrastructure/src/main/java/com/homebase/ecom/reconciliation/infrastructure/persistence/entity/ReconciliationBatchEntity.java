package com.homebase.ecom.reconciliation.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA entity for reconciliation_batches table.
 * Maps all columns from db-migrations reconciliation changelog.
 */
@Entity
@Table(name = "reconciliation_batches", indexes = {
        @Index(name = "idx_rb_batch_date", columnList = "batch_date"),
        @Index(name = "idx_rb_gateway_date", columnList = "gateway_type, batch_date"),
        @Index(name = "idx_rb_state", columnList = "state_id")
})
public class ReconciliationBatchEntity extends AbstractJpaStateEntity {

    @Column(name = "batch_date", nullable = false)
    private LocalDate batchDate;

    @Column(name = "period_start", nullable = false)
    private LocalDateTime periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDateTime periodEnd;

    @Column(name = "gateway_type", nullable = false, length = 50)
    private String gatewayType;

    @Column(name = "reconciliation_method", length = 30)
    private String reconciliationMethod;

    @Column(name = "matched_count")
    private int matchedCount;

    @Column(name = "mismatch_count")
    private int mismatchCount;

    @Column(name = "auto_resolved_count")
    private int autoResolvedCount;

    @Column(name = "unresolved_count")
    private int unresolvedCount;

    @Column(name = "total_gateway_amount", precision = 19, scale = 2)
    private BigDecimal totalGatewayAmount;

    @Column(name = "total_system_amount", precision = 19, scale = 2)
    private BigDecimal totalSystemAmount;

    @Column(name = "variance_amount", precision = 19, scale = 2)
    private BigDecimal varianceAmount;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "error_message", length = 2000)
    private String errorMessage;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "batch_id")
    private List<ReconciliationBatchActivityLogEntity> activities = new ArrayList<>();

    // --- Getters & Setters ---

    public LocalDate getBatchDate() { return batchDate; }
    public void setBatchDate(LocalDate batchDate) { this.batchDate = batchDate; }

    public LocalDateTime getPeriodStart() { return periodStart; }
    public void setPeriodStart(LocalDateTime periodStart) { this.periodStart = periodStart; }

    public LocalDateTime getPeriodEnd() { return periodEnd; }
    public void setPeriodEnd(LocalDateTime periodEnd) { this.periodEnd = periodEnd; }

    public String getGatewayType() { return gatewayType; }
    public void setGatewayType(String gatewayType) { this.gatewayType = gatewayType; }

    public String getReconciliationMethod() { return reconciliationMethod; }
    public void setReconciliationMethod(String reconciliationMethod) { this.reconciliationMethod = reconciliationMethod; }

    public int getMatchedCount() { return matchedCount; }
    public void setMatchedCount(int matchedCount) { this.matchedCount = matchedCount; }

    public int getMismatchCount() { return mismatchCount; }
    public void setMismatchCount(int mismatchCount) { this.mismatchCount = mismatchCount; }

    public int getAutoResolvedCount() { return autoResolvedCount; }
    public void setAutoResolvedCount(int autoResolvedCount) { this.autoResolvedCount = autoResolvedCount; }

    public int getUnresolvedCount() { return unresolvedCount; }
    public void setUnresolvedCount(int unresolvedCount) { this.unresolvedCount = unresolvedCount; }

    public BigDecimal getTotalGatewayAmount() { return totalGatewayAmount; }
    public void setTotalGatewayAmount(BigDecimal totalGatewayAmount) { this.totalGatewayAmount = totalGatewayAmount; }

    public BigDecimal getTotalSystemAmount() { return totalSystemAmount; }
    public void setTotalSystemAmount(BigDecimal totalSystemAmount) { this.totalSystemAmount = totalSystemAmount; }

    public BigDecimal getVarianceAmount() { return varianceAmount; }
    public void setVarianceAmount(BigDecimal varianceAmount) { this.varianceAmount = varianceAmount; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public List<ReconciliationBatchActivityLogEntity> getActivities() { return activities; }
    public void setActivities(List<ReconciliationBatchActivityLogEntity> activities) { this.activities = activities; }
}
