package com.homebase.ecom.payment.domain.port;

import com.homebase.ecom.payment.domain.model.Payment;

/**
 * Domain port for publishing payment lifecycle events.
 * Infrastructure adapter handles serialization, topics, and delivery mechanism.
 */
public interface PaymentEventPublisherPort {

    /**
     * Publishes PAYMENT_SUCCEEDED event when payment completes successfully.
     * Consumed by order service to transition order to PAID state.
     */
    void publishPaymentSucceeded(Payment payment);

    /**
     * Publishes PAYMENT_FAILED event when payment fails at gateway.
     * Consumed by order service to handle payment failure.
     */
    void publishPaymentFailed(Payment payment);

    /**
     * Publishes PAYMENT_FAILED event when payment is abandoned after max retries.
     * Terminal failure — no further retry attempts.
     */
    void publishPaymentAbandoned(Payment payment);

    /**
     * Publishes PAYMENT_REFUNDED event when refund completes.
     * Consumed by order and settlement services.
     */
    void publishPaymentRefunded(Payment payment);
}
