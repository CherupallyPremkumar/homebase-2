package com.homebase.ecom.payment.service.postSaveHooks;

import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.domain.port.PaymentEventPublisherPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PostSaveHook for ABANDONED state.
 * Publishes PAYMENT_FAILED (terminal failure) event after max retries exhausted.
 */
public class ABANDONEDPaymentPostSaveHook implements PostSaveHook<Payment> {

    private static final Logger log = LoggerFactory.getLogger(ABANDONEDPaymentPostSaveHook.class);

    private final PaymentEventPublisherPort eventPublisher;

    public ABANDONEDPaymentPostSaveHook(PaymentEventPublisherPort eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, Payment payment, TransientMap map) {
        log.warn("Payment {} entered ABANDONED state for orderId={}, retryCount={}",
                payment.getId(), payment.getOrderId(), payment.getRetryCount());
        eventPublisher.publishPaymentAbandoned(payment);
    }
}
