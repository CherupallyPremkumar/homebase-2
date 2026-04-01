package com.homebase.ecom.checkout.domain.port;

import com.homebase.ecom.shared.Money;

/**
 * Port for initiating/cancelling payments during checkout.
 * Adapter calls Payment service (which internally talks to Razorpay).
 */
public interface PaymentInitiationPort {

    /**
     * Creates a payment session for the checkout.
     * @return payment initiation result with paymentId and gateway details
     */
    PaymentResult initiatePayment(String checkoutId, String orderId,
                                   Money amount, String customerId,
                                   String paymentMethodId);

    /**
     * Cancels the payment session — used for compensation.
     */
    void cancelPayment(String paymentId);

    record PaymentResult(
        String paymentId,
        String gatewayOrderId,
        String status
    ) {}
}
