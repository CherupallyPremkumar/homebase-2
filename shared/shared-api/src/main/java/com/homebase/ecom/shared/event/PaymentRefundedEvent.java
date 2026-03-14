package com.homebase.ecom.shared.event;

import com.homebase.ecom.shared.Money;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentRefundedEvent {

    public static final String EVENT_TYPE = "PAYMENT_REFUNDED";

    private String orderId;

    /**
     * Legacy field name kept for backward compatibility.
     * Prefer using gatewayTransactionId.
     */
    private String paymentIntentId;

    /**
     * Gateway transaction id (Stripe payment_intent, Razorpay order/payment id, etc).
     */
    private String gatewayTransactionId;

    private BigDecimal refundedAmount;

    /**
     * Optional enriched money amount (rupees scale=2).
     */
    private Money refundedMoney;

    private String refundReason;
    private LocalDateTime timestamp;

    public PaymentRefundedEvent() {
    }

    public PaymentRefundedEvent(String orderId, String gatewayTransactionId, BigDecimal refundedAmount, String refundReason,
            LocalDateTime timestamp) {
        this.orderId = orderId;
        // keep both fields in sync
        this.paymentIntentId = gatewayTransactionId;
        this.gatewayTransactionId = gatewayTransactionId;
        this.refundedAmount = refundedAmount;
        this.refundReason = refundReason;
        this.timestamp = timestamp;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentIntentId() {
        return paymentIntentId;
    }

    public void setPaymentIntentId(String paymentIntentId) {
        this.paymentIntentId = paymentIntentId;
        this.gatewayTransactionId = paymentIntentId;
    }

    public String getGatewayTransactionId() {
        return gatewayTransactionId != null ? gatewayTransactionId : paymentIntentId;
    }

    public void setGatewayTransactionId(String gatewayTransactionId) {
        this.gatewayTransactionId = gatewayTransactionId;
        this.paymentIntentId = gatewayTransactionId;
    }

    public BigDecimal getRefundedAmount() {
        return refundedAmount;
    }

    public void setRefundedAmount(BigDecimal refundedAmount) {
        this.refundedAmount = refundedAmount;
    }

    public Money getRefundedMoney() {
        return refundedMoney;
    }

    public void setRefundedMoney(Money refundedMoney) {
        this.refundedMoney = refundedMoney;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
