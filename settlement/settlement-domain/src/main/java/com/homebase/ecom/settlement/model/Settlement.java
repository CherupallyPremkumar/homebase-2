package com.homebase.ecom.settlement.model;

import org.chenile.utils.entity.model.AbstractExtendedStateEntity;
import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;
import com.homebase.ecom.shared.Money;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Settlement bounded context aggregate root.
 * Tracks supplier payouts for completed orders with commission and fee calculations.
 *
 * Fields per requirement:
 * id, supplierId, orderId, orderAmount, commissionAmount, platformFee, netAmount,
 * currency, settlementPeriodStart, settlementPeriodEnd, adjustments(List),
 * disbursementReference, stateId, flowId
 */
public class Settlement extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity, ContainsTransientMap {

    private String description;
    private String supplierId;
    private String orderId;
    private Money orderAmount;
    private Money commissionAmount;
    private Money platformFee;
    private Money netAmount;
    private String currency;
    private LocalDate settlementPeriodStart;
    private LocalDate settlementPeriodEnd;
    private List<SettlementAdjustment> adjustments = new ArrayList<>();
    private String disbursementReference;
    private String tenant;

    private transient TransientMap transientMap = new TransientMap();
    private List<ActivityLog> activities = new ArrayList<>();

    // --- Getters & Setters ---

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public Money getOrderAmount() { return orderAmount; }
    public void setOrderAmount(Money orderAmount) { this.orderAmount = orderAmount; }

    public Money getCommissionAmount() { return commissionAmount; }
    public void setCommissionAmount(Money commissionAmount) { this.commissionAmount = commissionAmount; }

    public Money getPlatformFee() { return platformFee; }
    public void setPlatformFee(Money platformFee) { this.platformFee = platformFee; }

    public Money getNetAmount() { return netAmount; }
    public void setNetAmount(Money netAmount) { this.netAmount = netAmount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public LocalDate getSettlementPeriodStart() { return settlementPeriodStart; }
    public void setSettlementPeriodStart(LocalDate settlementPeriodStart) { this.settlementPeriodStart = settlementPeriodStart; }

    public LocalDate getSettlementPeriodEnd() { return settlementPeriodEnd; }
    public void setSettlementPeriodEnd(LocalDate settlementPeriodEnd) { this.settlementPeriodEnd = settlementPeriodEnd; }

    public List<SettlementAdjustment> getAdjustments() { return adjustments; }
    public void setAdjustments(List<SettlementAdjustment> adjustments) { this.adjustments = adjustments; }

    public String getDisbursementReference() { return disbursementReference; }
    public void setDisbursementReference(String disbursementReference) { this.disbursementReference = disbursementReference; }

    @Override
    public TransientMap getTransientMap() { return transientMap; }

    @Override
    public Collection<ActivityLog> obtainActivities() {
        return new ArrayList<>(activities);
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        SettlementActivityLog log = new SettlementActivityLog();
        log.activityName = eventId;
        log.activityComment = comment;
        log.activitySuccess = true;
        activities.add(log);
        return log;
    }

    /**
     * Calculate total adjustment amount.
     */
    public BigDecimal getTotalAdjustmentAmount() {
        return adjustments.stream()
                .map(SettlementAdjustment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }
}
