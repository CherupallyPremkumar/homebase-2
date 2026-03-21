package com.homebase.ecom.payment.infrastructure.adapter;

import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.domain.port.NotificationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default notification adapter that logs notification events.
 * In production, this would be replaced with an adapter that sends
 * real notifications via email/SMS/push services.
 */
public class LoggingNotificationAdapter implements NotificationPort {

    private static final Logger log = LoggerFactory.getLogger(LoggingNotificationAdapter.class);

    @Override
    public void notifyPaymentSucceeded(Payment payment) {
        log.info("NOTIFICATION: Payment succeeded for customerId={}, orderId={}, amount={}",
                payment.getCustomerId(), payment.getOrderId(), payment.getAmount());
    }

    @Override
    public void notifyPaymentFailed(Payment payment) {
        log.warn("NOTIFICATION: Payment failed for customerId={}, orderId={}, reason={}",
                payment.getCustomerId(), payment.getOrderId(), payment.getFailureReason());
    }

    @Override
    public void notifyRefundCompleted(Payment payment) {
        log.info("NOTIFICATION: Refund completed for customerId={}, orderId={}, amount={}",
                payment.getCustomerId(), payment.getOrderId(), payment.getAmount());
    }
}
