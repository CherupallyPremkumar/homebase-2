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

    // ── Payment Method Details ──────────────────────────────────────────────
    private String paymentMethodType; // CREDIT_CARD, DEBIT_CARD, UPI, NET_BANKING, WALLET
    private String cardLastFour;
    private String cardBrand;
    private String upiId;

    // ── Gateway ────────────────────────────────────────────────────────────
    private String gatewayName;
    private String gatewayOrderId;
    private String gatewayPaymentId;
    private String gatewayTransactionId;
    private String gatewayResponse;

    // ── Checkout / Idempotency ──────────────────────────────────────────────
    private String checkoutId;
    private String idempotencyKey;

    // ── Refund ──────────────────────────────────────────────────────────────
    private BigDecimal refundAmount = BigDecimal.ZERO;
    private String refundReason;

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

    public String getPaymentMethodType() { return paymentMethodType; }
    public void setPaymentMethodType(String paymentMethodType) { this.paymentMethodType = paymentMethodType; }

    public String getCardLastFour() { return cardLastFour; }
    public void setCardLastFour(String cardLastFour) { this.cardLastFour = cardLastFour; }

    public String getCardBrand() { return cardBrand; }
    public void setCardBrand(String cardBrand) { this.cardBrand = cardBrand; }

    public String getUpiId() { return upiId; }
    public void setUpiId(String upiId) { this.upiId = upiId; }

    public String getGatewayName() { return gatewayName; }
    public void setGatewayName(String gatewayName) { this.gatewayName = gatewayName; }

    public String getGatewayOrderId() { return gatewayOrderId; }
    public void setGatewayOrderId(String gatewayOrderId) { this.gatewayOrderId = gatewayOrderId; }

    public String getGatewayPaymentId() { return gatewayPaymentId; }
    public void setGatewayPaymentId(String gatewayPaymentId) { this.gatewayPaymentId = gatewayPaymentId; }

    public String getGatewayTransactionId() { return gatewayTransactionId; }
    public void setGatewayTransactionId(String gatewayTransactionId) { this.gatewayTransactionId = gatewayTransactionId; }

    public String getGatewayResponse() { return gatewayResponse; }
    public void setGatewayResponse(String gatewayResponse) { this.gatewayResponse = gatewayResponse; }

    public String getCheckoutId() { return checkoutId; }
    public void setCheckoutId(String checkoutId) { this.checkoutId = checkoutId; }

    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }

    public BigDecimal getRefundAmount() { return refundAmount; }
    public void setRefundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; }

    public String getRefundReason() { return refundReason; }
    public void setRefundReason(String refundReason) { this.refundReason = refundReason; }

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
