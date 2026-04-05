package com.homebase.ecom.checkout.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "checkout")
public class CheckoutEntity extends AbstractJpaStateEntity {

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "cart_id")
    private String cartId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "payment_id")
    private String paymentId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "checkout", orphanRemoval = true)
    private List<CheckoutItemEntity> items = new ArrayList<>();

    @Column(name = "subtotal")
    private long subtotal;

    @Column(name = "currency", length = 3)
    private String currency = "INR";

    @Column(name = "discount_amount")
    private long discountAmount;

    @Column(name = "shipping_cost")
    private long shippingCost;

    @Column(name = "tax_amount")
    private long taxAmount;

    @Column(name = "total")
    private long total;

    @Column(name = "coupon_codes", length = 500)
    private String couponCodes;

    @Column(name = "shipping_address_id")
    private String shippingAddressId;

    @Column(name = "billing_address_id")
    private String billingAddressId;

    @Column(name = "shipping_method")
    private String shippingMethod;

    @Column(name = "payment_method_id")
    private String paymentMethodId;

    @Column(name = "last_completed_step")
    private String lastCompletedStep;

    @Column(name = "failure_reason", length = 1000)
    private String failureReason;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "description", length = 2000)
    public String description;

    @Column(name = "idempotency_key")
    private String idempotencyKey;

    @Column(name = "price_lock_token")
    private String priceLockToken;

    @Column(name = "payment_session_id")
    private String paymentSessionId;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    // ── Getters & Setters ──────────────────────────────────────────────

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getCartId() { return cartId; }
    public void setCartId(String cartId) { this.cartId = cartId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public List<CheckoutItemEntity> getItems() { return items; }
    public void setItems(List<CheckoutItemEntity> items) { this.items = items; }

    public long getSubtotal() { return subtotal; }
    public void setSubtotal(long subtotal) { this.subtotal = subtotal; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public long getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(long discountAmount) { this.discountAmount = discountAmount; }

    public long getShippingCost() { return shippingCost; }
    public void setShippingCost(long shippingCost) { this.shippingCost = shippingCost; }

    public long getTaxAmount() { return taxAmount; }
    public void setTaxAmount(long taxAmount) { this.taxAmount = taxAmount; }

    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }

    public String getCouponCodes() { return couponCodes; }
    public void setCouponCodes(String couponCodes) { this.couponCodes = couponCodes; }

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
}
