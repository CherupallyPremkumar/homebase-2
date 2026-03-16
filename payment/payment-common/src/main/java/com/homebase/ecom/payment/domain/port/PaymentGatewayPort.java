package com.homebase.ecom.payment.domain.port;

import com.homebase.ecom.payment.domain.model.Payment;

/**
 * Port for interacting with external payment gateways (Stripe, Razorpay, etc.).
 * Adapters implement this for each gateway provider.
 */
public interface PaymentGatewayPort {

    record GatewayResult(boolean success, String gatewayTransactionId, String gatewayResponse, String failureReason) {}

    GatewayResult processPayment(Payment payment);

    GatewayResult processRefund(Payment payment);
}
