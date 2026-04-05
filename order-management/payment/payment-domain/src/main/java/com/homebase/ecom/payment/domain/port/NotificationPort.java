package com.homebase.ecom.payment.domain.port;

import com.homebase.ecom.payment.domain.model.Payment;

/**
 * Port for sending payment-related notifications (email, SMS, push).
 * Adapters implement this for each notification channel.
 */
public interface NotificationPort {

    void notifyPaymentSucceeded(Payment payment);

    void notifyPaymentFailed(Payment payment);

    void notifyRefundCompleted(Payment payment);
}
