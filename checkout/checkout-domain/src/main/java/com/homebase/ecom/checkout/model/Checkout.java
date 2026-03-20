package com.homebase.ecom.checkout.model;

import com.homebase.ecom.shared.Money;
import org.chenile.utils.entity.model.AbstractExtendedStateEntity;
import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Checkout bounded context domain model.
 * Orchestrates the checkout saga: lock cart → lock price → reserve inventory
 * → validate shipping → create order → commit promo → initiate payment.
 *
 * Extends Chenile's AbstractExtendedStateEntity for STM integration.
 */
public class Checkout extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity, ContainsTransientMap {

    private String customerId;
    private String cartId;

    // Snapshot data captured during saga
    private String orderId;
    private String paymentId;

    // Cart snapshot (items, totals captured at checkout time)
    private List<CheckoutItem> items = new ArrayList<>();
    private Money subtotal = Money.ZERO_INR;
    private Money discountAmount = Money.ZERO_INR;
    private Money shippingCost = Money.ZERO_INR;
    private Money taxAmount = Money.ZERO_INR;
    private Money total = Money.ZERO_INR;
    private List<String> couponCodes = new ArrayList<>();

    // Shipping & billing
    private String shippingAddressId;
    private String billingAddressId;
    private String shippingMethod;

    // Payment
    private String paymentMethodId;

    // Saga tracking
    private String lastCompletedStep;
    private String failureReason;
    private LocalDateTime expiresAt;

    // Idempotency and locks (from checkout-003 migration)
    private String idempotencyKey;
    private String priceLockToken;
    private String paymentSessionId;
    private LocalDateTime completedAt;
    private LocalDateTime cancelledAt;

    // Retry tracking for CHECK_RETRY_ALLOWED auto-state
    private int paymentRetryCount;

    public String description;
    private String tenant;

    private List<CheckoutActivityLog> activities = new ArrayList<>();
    public TransientMap transientMap = new TransientMap();

    // ── Getters & Setters ──────────────────────────────────────────────

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getCartId() { return cartId; }
    public void setCartId(String cartId) { this.cartId = cartId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public List<CheckoutItem> getItems() { return items; }
    public void setItems(List<CheckoutItem> items) { this.items = items; }

    public Money getSubtotal() { return subtotal; }
    public void setSubtotal(Money subtotal) { this.subtotal = subtotal; }

    public Money getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(Money discountAmount) { this.discountAmount = discountAmount; }

    public Money getShippingCost() { return shippingCost; }
    public void setShippingCost(Money shippingCost) { this.shippingCost = shippingCost; }

    public Money getTaxAmount() { return taxAmount; }
    public void setTaxAmount(Money taxAmount) { this.taxAmount = taxAmount; }

    public Money getTotal() { return total; }
    public void setTotal(Money total) { this.total = total; }

    public List<String> getCouponCodes() { return couponCodes; }
    public void setCouponCodes(List<String> couponCodes) { this.couponCodes = couponCodes; }

    public String getShippingAddressId() { return shippingAddressId; }
    public void setShippingAddressId(String shippingAddressId) { this.shippingAddressId = shippingAddressId; }

    public String getBillingAddressId() { return billingAddressId; }
    public void setBillingAddressId(String billingAddressId) { this.billingAddressId = billingAddressId; }

    public String getShippingMethod() { return shippingMethod; }
    public void setShippingMethod(String shippingMethod) { this.shippingMethod = shippingMethod; }

    public String getPaymentMethodId() { return paymentMethodId; }
    public void setPaymentMethodId(String paymentMethodId) { this.paymentMethodId = paymentMethodId; }

    public String getLastCompletedStep() { return lastCompletedStep; }
    public void setLastCompletedStep(String lastCompletedStep) { this.lastCompletedStep = lastCompletedStep; }

    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }

    public String getPriceLockToken() { return priceLockToken; }
    public void setPriceLockToken(String priceLockToken) { this.priceLockToken = priceLockToken; }

    public String getPaymentSessionId() { return paymentSessionId; }
    public void setPaymentSessionId(String paymentSessionId) { this.paymentSessionId = paymentSessionId; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public LocalDateTime getCancelledAt() { return cancelledAt; }
    public void setCancelledAt(LocalDateTime cancelledAt) { this.cancelledAt = cancelledAt; }

    public int getPaymentRetryCount() { return paymentRetryCount; }
    public void setPaymentRetryCount(int paymentRetryCount) { this.paymentRetryCount = paymentRetryCount; }

    public List<CheckoutActivityLog> getActivities() { return activities; }
    public void setActivities(List<CheckoutActivityLog> activities) { this.activities = activities; }

    @Override
    public java.util.Collection<ActivityLog> obtainActivities() {
        java.util.Collection<ActivityLog> acts = new ArrayList<>();
        for (ActivityLog a : activities) {
            acts.add(a);
        }
        return acts;
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        CheckoutActivityLog log = new CheckoutActivityLog();
        log.activityName = eventId;
        log.activityComment = comment;
        log.activitySuccess = true;
        activities.add(log);
        return log;
    }

    @Override
    public TransientMap getTransientMap() { return this.transientMap; }

    public void setTransientMap(TransientMap transientMap) { this.transientMap = transientMap; }

    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }
}
