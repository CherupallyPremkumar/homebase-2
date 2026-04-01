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
 * PostSaveHook for REFUNDED state.
 * Publishes PAYMENT_REFUNDED event for order and settlement service consumption.
 * Also triggers notification to customer.
 */
public class REFUNDEDPaymentPostSaveHook implements PostSaveHook<Payment> {

    private static final Logger log = LoggerFactory.getLogger(REFUNDEDPaymentPostSaveHook.class);

    private final PaymentEventPublisherPort eventPublisher;
    private final NotificationPort notificationPort;

    public REFUNDEDPaymentPostSaveHook(PaymentEventPublisherPort eventPublisher, NotificationPort notificationPort) {
        this.eventPublisher = eventPublisher;
        this.notificationPort = notificationPort;
    }

    @Override
    public void execute(State startState, State endState, Payment payment, TransientMap map) {
        // Publish to Kafka for order + settlement services
        log.info("Payment {} entered REFUNDED state for orderId={}, amount={}",
                payment.getId(), payment.getOrderId(), payment.getAmount());
        eventPublisher.publishPaymentRefunded(payment);

        // Notify customer
        if (notificationPort != null) {
            try {
                notificationPort.notifyRefundCompleted(payment);
            } catch (Exception e) {
                log.error("Failed to send refund notification for orderId={}", payment.getOrderId(), e);
            }
        }
    }
}
