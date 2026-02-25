package com.ecommerce.payment.gateway;

import com.ecommerce.payment.domain.WebhookEvent;

/**
 * Interface for processing webhooks from payment gateways.
 */
public interface WebhookProcessor {
    /**
     * Supports a specific gateway type?
     */
    boolean supports(String gatewayType);

    /**
     * Processes the raw webhook payload.
     * 
     * @param payload   The raw body from the gateway
     * @param signature The signature header (Stripe-Signature,
     *                  X-Razorpay-Signature, etc.)
     * @return A normalized gateway event
     */
    GatewayEvent process(String payload, String signature);
}
