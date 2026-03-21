package com.homebase.ecom.payment.domain.port;

import com.homebase.ecom.payment.domain.model.Payment;

import java.math.BigDecimal;

/**
 * Port for interacting with external payment gateways (Razorpay, Stripe, etc.).
 * Gateway-agnostic — adapters implement this for each provider.
 * Strategy pattern: GatewayRouter selects the right adapter at runtime.
 */
public interface PaymentGatewayPort {

    /** Result of a gateway session creation (checkout page URL / payment intent). */
    record GatewaySession(
            String gatewayOrderId,
            String sessionUrl,
            String sessionToken,
            long expiresInSeconds
    ) {}

    /** Result of verifying a payment after gateway callback/webhook. */
    record GatewayVerification(
            boolean success,
            String gatewayPaymentId,
            String gatewayTransactionId,
            String gatewayResponse,
            String failureReason,
            String failureCode
    ) {}

    /** Result of a refund request to the gateway. */
    record GatewayRefundResult(
            boolean success,
            String gatewayRefundId,
            String failureReason
    ) {}

    /** Result of tokenizing a payment instrument (for saved cards/UPI). */
    record TokenizationResult(
            boolean success,
            String gatewayToken,
            String maskedInstrument,
            String failureReason
    ) {}

    /**
     * Create a payment session with the gateway (Razorpay Order / Stripe PaymentIntent).
     * Frontend uses the returned sessionUrl/token to collect payment.
     */
    GatewaySession createSession(Payment payment);

    /**
     * Verify a payment after gateway callback (signature verification + status check).
     * Called when webhook confirms payment or customer returns from gateway page.
     */
    GatewayVerification verifyPayment(String gatewayOrderId, String gatewayPaymentId, String signature);

    /**
     * Submit a refund to the gateway.
     */
    GatewayRefundResult initiateRefund(String gatewayPaymentId, BigDecimal amount, String reason);

    /**
     * Tokenize a payment instrument for future use (saved cards, mandates).
     * Token is stored encrypted — raw card data never touches our servers.
     */
    TokenizationResult tokenizeInstrument(String customerId, String gatewayToken);

    /**
     * Check if this gateway supports the given payment method type.
     */
    boolean supportsMethod(String paymentMethodType);

    /**
     * Gateway identity (e.g., "RAZORPAY", "STRIPE").
     */
    String gatewayName();
}
