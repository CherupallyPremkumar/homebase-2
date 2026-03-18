package com.homebase.ecom.payment.domain.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.utils.entity.model.AbstractExtendedStateEntity;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;

/**
 * Payment bounded context domain model.
 * Tracks a single payment attempt for an order through the STM lifecycle.
 *
 * Fields: id, orderId, customerId, amount, currency, paymentMethod,
 * gatewayTransactionId, gatewayResponse, retryCount, failureReason,
 * stateId (via currentState), flowId (via currentState).
 */
public class Payment extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity, ContainsTransientMap {

    // ── Core Identity ──────────────────────────────────────────────────────
    private String orderId;
    private String customerId;

    // ── Amount ──────────────────────────────────────────────────────────────
    private BigDecimal amount = BigDecimal.ZERO;
    private String currency = "INR";

    // ── Payment Method ─────────────────────────────────────────────────────
    private String paymentMethod; // CARD, UPI, NET_BANKING, WALLET

    // ── Gateway ────────────────────────────────────────────────────────────
    private String gatewayTransactionId;
    private String gatewayResponse;

    // ── Retry / Failure ────────────────────────────────────────────────────
    private int retryCount = 0;
    private String failureReason;
    private String tenant;

    // ── Workflow ────────────────────────────────────────────────────────────
    private transient TransientMap transientMap = new TransientMap();
    private List<ActivityLog> activities = new ArrayList<>();

    // ── Getters and Setters ────────────────────────────────────────────────

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getGatewayTransactionId() { return gatewayTransactionId; }
    public void setGatewayTransactionId(String gatewayTransactionId) { this.gatewayTransactionId = gatewayTransactionId; }

    public String getGatewayResponse() { return gatewayResponse; }
    public void setGatewayResponse(String gatewayResponse) { this.gatewayResponse = gatewayResponse; }

    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }

    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }

    // ── Business Logic ─────────────────────────────────────────────────────

    public void incrementRetryCount() {
        this.retryCount++;
        this.setLastModifiedTime(new java.util.Date());
    }

    public void recordSuccess(String gatewayTransactionId, String gatewayResponse) {
        this.gatewayTransactionId = gatewayTransactionId;
        this.gatewayResponse = gatewayResponse;
        this.failureReason = null;
        this.setLastModifiedTime(new java.util.Date());
    }

    public void recordFailure(String failureReason, String gatewayResponse) {
        this.failureReason = failureReason;
        this.gatewayResponse = gatewayResponse;
        this.setLastModifiedTime(new java.util.Date());
    }

    // ── Workflow interface ──────────────────────────────────────────────────

    @Override
    public TransientMap getTransientMap() { return transientMap; }

    public List<ActivityLog> getActivities() { return activities; }
    public void setActivities(List<ActivityLog> activities) { this.activities = activities; }

    @Override
    public Collection<ActivityLog> obtainActivities() {
        return new ArrayList<>(activities);
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        PaymentActivityLogEntry activityLog = new PaymentActivityLogEntry();
        activityLog.activityName = eventId;
        activityLog.activityComment = comment;
        activityLog.activitySuccess = true;
        this.activities.add(activityLog);
        return activityLog;
    }

    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }
}
