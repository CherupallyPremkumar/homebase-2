package com.homebase.ecom.payment.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA entity for payment persistence.
 * Maps to the payment table — all columns from db-migrations payment changesets.
 */
@Entity
@Table(name = "payment")
public class PaymentEntity extends AbstractJpaStateEntity {

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency", length = 3)
    private String currency = "INR";

    @Column(name = "payment_method", length = 20)
    private String paymentMethod;

    // ── Payment Method Details (changeset 003) ──────────────────────────────
    @Column(name = "payment_method_type", length = 50)
    private String paymentMethodType;

    @Column(name = "card_last_four", length = 4)
    private String cardLastFour;

    @Column(name = "card_brand", length = 50)
    private String cardBrand;

    @Column(name = "upi_id")
    private String upiId;

    // ── Gateway ─────────────────────────────────────────────────────────────
    @Column(name = "gateway_name", length = 50)
    private String gatewayName;

    @Column(name = "gateway_order_id")
    private String gatewayOrderId;

    @Column(name = "gateway_payment_id")
    private String gatewayPaymentId;

    @Column(name = "gateway_transaction_id")
    private String gatewayTransactionId;

    @Column(name = "gateway_response", columnDefinition = "TEXT")
    private String gatewayResponse;

    // ── Checkout / Idempotency (changeset 003) ──────────────────────────────
    @Column(name = "checkout_id")
    private String checkoutId;

    @Column(name = "idempotency_key", unique = true)
    private String idempotencyKey;

    // ── Refund (changeset 003) ──────────────────────────────────────────────
    @Column(name = "refund_amount", precision = 12, scale = 2)
    private BigDecimal refundAmount = BigDecimal.ZERO;

    @Column(name = "refund_reason", length = 500)
    private String refundReason;

    // ── Retry / Failure ─────────────────────────────────────────────────────
    @Column(name = "retry_count")
    private int retryCount = 0;

    @Column(name = "failure_reason", columnDefinition = "TEXT")
    private String failureReason;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_id")
    private List<PaymentActivityLogEntity> activities = new ArrayList<>();

    // ── Getters and Setters ─────────────────────────────────────────────────

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

    public List<PaymentActivityLogEntity> getActivities() { return activities; }
    public void setActivities(List<PaymentActivityLogEntity> activities) { this.activities = activities; }
}
