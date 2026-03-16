package com.homebase.ecom.order.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for paymentSucceeded event — triggered by payment.events PAYMENT_SUCCEEDED.
 */
public class PaymentSucceededOrderPayload extends MinimalPayload {
    private String paymentId;
    private String transactionId;

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
}
