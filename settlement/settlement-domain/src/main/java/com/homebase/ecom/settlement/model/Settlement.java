package com.homebase.ecom.settlement.model;

import org.chenile.utils.entity.model.AbstractExtendedStateEntity;
import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;
import com.homebase.ecom.shared.Money;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Settlement bounded context aggregate root.
 * Tracks supplier payouts for completed orders with commission and fee calculations.
 *
 * DB columns (source of truth: db-migrations settlement changelog):
 * Base: id, created_time, last_modified_time, last_modified_by, tenant, created_by, version
 * STM: state_entry_time, sla_yellow_date, sla_red_date, sla_tending_late, sla_late, flow_id, state_id
 * Business: description, supplier_id, order_id, order_amount, commission_amount, platform_fee,
 *   net_amount, currency, settlement_period_start, settlement_period_end, disbursement_reference,
 *   payment_id, settlement_number, tax_amount, adjustment_amount, bank_account_id,
 *   disbursement_date, disbursement_method
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

    // Fields from changeset settlement-004
    private String paymentId;
    private String settlementNumber;
    private Money taxAmount;
    private Money adjustmentAmount;
    private String bankAccountId;
    private LocalDateTime disbursementDate;
    private String disbursementMethod;

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

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public String getSettlementNumber() { return settlementNumber; }
    public void setSettlementNumber(String settlementNumber) { this.settlementNumber = settlementNumber; }

    public Money getTaxAmount() { return taxAmount; }
    public void setTaxAmount(Money taxAmount) { this.taxAmount = taxAmount; }

    public Money getAdjustmentAmount() { return adjustmentAmount; }
    public void setAdjustmentAmount(Money adjustmentAmount) { this.adjustmentAmount = adjustmentAmount; }

    public String getBankAccountId() { return bankAccountId; }
    public void setBankAccountId(String bankAccountId) { this.bankAccountId = bankAccountId; }

    public LocalDateTime getDisbursementDate() { return disbursementDate; }
    public void setDisbursementDate(LocalDateTime disbursementDate) { this.disbursementDate = disbursementDate; }

    public String getDisbursementMethod() { return disbursementMethod; }
    public void setDisbursementMethod(String disbursementMethod) { this.disbursementMethod = disbursementMethod; }

    @Override
    public TransientMap getTransientMap() { return transientMap; }

    @Override
    public Collection<ActivityLog> obtainActivities() {
        return new ArrayList<>(activities);
    }

    /**
     * Direct access to the activities list for JSON serialization.
     * Chenile BDD tests assert on "mutatedEntity.activities" via JSON path.
     */
    public List<ActivityLog> getActivities() { return activities; }
    public void setActivities(List<ActivityLog> activities) { this.activities = activities; }

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
     * Calculate total adjustment amount in smallest currency unit (paise).
     */
    public long getTotalAdjustmentAmountPaise() {
        return adjustments.stream()
                .map(SettlementAdjustment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .longValue();
    }

    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }
}
