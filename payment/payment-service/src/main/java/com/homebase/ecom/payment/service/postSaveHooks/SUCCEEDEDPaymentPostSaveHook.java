package com.homebase.ecom.payment.service.postSaveHooks;

import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.domain.port.PaymentEventPublisherPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PostSaveHook for SUCCEEDED state.
 * Publishes PAYMENT_SUCCEEDED event for order service consumption.
 */
public class SUCCEEDEDPaymentPostSaveHook implements PostSaveHook<Payment> {

    private static final Logger log = LoggerFactory.getLogger(SUCCEEDEDPaymentPostSaveHook.class);

    private final PaymentEventPublisherPort eventPublisher;

    public SUCCEEDEDPaymentPostSaveHook(PaymentEventPublisherPort eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, Payment payment, TransientMap map) {
        log.info("Payment {} entered SUCCEEDED state for orderId={}", payment.getId(), payment.getOrderId());
        eventPublisher.publishPaymentSucceeded(payment);
    }
}
