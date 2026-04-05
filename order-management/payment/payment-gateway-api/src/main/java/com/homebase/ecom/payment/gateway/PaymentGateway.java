package com.homebase.ecom.payment.gateway;

import com.homebase.ecom.payment.dto.CheckoutSessionRequest;
import com.homebase.ecom.payment.dto.CheckoutSessionResponse;
import java.math.BigDecimal;

/**
 * Common interface for all payment gateway implementations.
 */
public interface PaymentGateway {

    /**
     * Returns the gateway identifier (e.g., "STRIPE", "RAZORPAY").
     */
    String getGatewayType();

    /**
     * Creates a hosted checkout session.
     */
    CheckoutSessionResponse createCheckoutSession(CheckoutSessionRequest request);

    /**
     * Creates a payment intent for on-site payment processing.
     * 
     * @return Gateway-specific identifier (e.g., Client Secret or Order ID)
     */
    String createPaymentIntent(String orderId, BigDecimal amount, String currency);

    /**
     * Processes a refund.
     */
    void processRefund(String transactionId, BigDecimal amount, String reason);
}
