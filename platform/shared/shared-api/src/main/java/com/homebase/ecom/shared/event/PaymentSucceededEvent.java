package com.homebase.ecom.shared.event;

import com.homebase.ecom.shared.Money;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Kafka event DTO published when payment succeeds via Stripe webhook.
 */
public class PaymentSucceededEvent implements Serializable {

    public static final String EVENT_TYPE = "PAYMENT_SUCCEEDED";

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

    /**
     * Optional enriched money amount (rupees scale=2).
     */
    private Money amount;

    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp;

    public PaymentSucceededEvent() {
    }

    public PaymentSucceededEvent(String orderId, String gatewayTransactionId, LocalDateTime timestamp) {
        this.orderId = orderId;
        // keep both fields in sync
        this.paymentIntentId = gatewayTransactionId;
        this.gatewayTransactionId = gatewayTransactionId;
        this.timestamp = timestamp;
        this.eventType = EVENT_TYPE; // Initialize eventType in the constructor
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

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
