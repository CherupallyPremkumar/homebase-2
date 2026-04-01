package com.homebase.ecom.reconciliation.model;

import org.chenile.utils.entity.model.AbstractExtendedStateEntity;
import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Reconciliation batch aggregate root.
 * Tracks a reconciliation run comparing gateway transactions against system transactions.
 *
 * DB columns (source of truth: db-migrations reconciliation changelog):
 * Base: id, created_time, last_modified_time, last_modified_by, tenant, created_by, version
 * STM: state_entry_time, sla_yellow_date, sla_red_date, sla_tending_late, sla_late, flow_id, state_id
 * Business: batch_date, period_start, period_end, gateway_type, reconciliation_method,
 *   matched_count, mismatch_count, auto_resolved_count, unresolved_count,
 *   total_gateway_amount, total_system_amount, variance_amount, completed_at, error_message
 */
public class ReconciliationBatch extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity, ContainsTransientMap {

    private String tenant;
    private LocalDate batchDate;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private String gatewayType;
    private String reconciliationMethod;
    private int matchedCount;
    private int mismatchCount;
    private int autoResolvedCount;
    private int unresolvedCount;
    private BigDecimal totalGatewayAmount;
    private BigDecimal totalSystemAmount;
    private BigDecimal varianceAmount;
    private LocalDateTime completedAt;
    private String errorMessage;

    // OGNL auto-state field: used by CHECK_MISMATCHES auto-state
    private Integer unresolvedMismatchCount;

    private transient TransientMap transientMap = new TransientMap();
    private List<ActivityLog> activities = new ArrayList<>();

    // --- Getters & Setters ---

    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }

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

    public Integer getUnresolvedMismatchCount() { return unresolvedMismatchCount; }
    public void setUnresolvedMismatchCount(Integer unresolvedMismatchCount) { this.unresolvedMismatchCount = unresolvedMismatchCount; }

    @Override
    public TransientMap getTransientMap() { return transientMap; }

    @Override
    public Collection<ActivityLog> obtainActivities() {
        return new ArrayList<>(activities);
    }

    public List<ActivityLog> getActivities() { return activities; }
    public void setActivities(List<ActivityLog> activities) { this.activities = activities; }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        ReconciliationBatchActivityLog log = new ReconciliationBatchActivityLog();
        log.activityName = eventId;
        log.activityComment = comment;
        log.activitySuccess = true;
        activities.add(log);
        return log;
    }
}
