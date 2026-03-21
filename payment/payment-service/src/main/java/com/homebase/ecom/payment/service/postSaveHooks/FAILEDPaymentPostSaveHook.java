package com.homebase.ecom.payment.service.postSaveHooks;

import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.domain.port.NotificationPort;
import com.homebase.ecom.payment.domain.port.PaymentEventPublisherPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PostSaveHook for FAILED state.
 * Publishes PAYMENT_FAILED event for order service consumption.
 * Also triggers notification to customer.
 */
public class FAILEDPaymentPostSaveHook implements PostSaveHook<Payment> {

    private static final Logger log = LoggerFactory.getLogger(FAILEDPaymentPostSaveHook.class);

    private final PaymentEventPublisherPort eventPublisher;
    private final NotificationPort notificationPort;

    public FAILEDPaymentPostSaveHook(PaymentEventPublisherPort eventPublisher, NotificationPort notificationPort) {
        this.eventPublisher = eventPublisher;
        this.notificationPort = notificationPort;
    }

    @Override
    public void execute(State startState, State endState, Payment payment, TransientMap map) {
        // Publish to Kafka
        log.warn("Payment {} entered FAILED state for orderId={}, reason={}",
                payment.getId(), payment.getOrderId(), payment.getFailureReason());
        eventPublisher.publishPaymentFailed(payment);

        // Notify customer
        if (notificationPort != null) {
            try {
                notificationPort.notifyPaymentFailed(payment);
            } catch (Exception e) {
                log.error("Failed to send payment failure notification for orderId={}", payment.getOrderId(), e);
            }
        }
    }
}
