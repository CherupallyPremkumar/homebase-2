package com.homebase.ecom.payment.service.cmds;

import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.dto.InitiatePaymentPayload;
import com.homebase.ecom.payment.service.validator.PaymentPolicyValidator;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * STM action for initiating a payment.
 * Validates amount limits and payment method via PolicyValidator,
 * then populates the Payment domain model.
 */
public class InitiatePaymentAction extends AbstractSTMTransitionAction<Payment, InitiatePaymentPayload> {

    private static final Logger log = LoggerFactory.getLogger(InitiatePaymentAction.class);

    @Autowired
    private PaymentPolicyValidator policyValidator;

    @Override
    public void transitionTo(Payment payment, InitiatePaymentPayload payload, State startState,
            String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Policy enforcement: amount limits
        policyValidator.validateAmount(payload.getAmount());

        // Policy enforcement: payment method
        policyValidator.validatePaymentMethod(payload.getPaymentMethod());

        // Populate domain model
        payment.setOrderId(payload.getOrderId());
        payment.setCustomerId(payload.getCustomerId());
        payment.setAmount(payload.getAmount());
        payment.setCurrency(payload.getCurrency() != null ? payload.getCurrency() : "INR");
        payment.setPaymentMethod(payload.getPaymentMethod());

        log.info("Payment initiated for orderId={}, amount={}, method={}",
                payload.getOrderId(), payload.getAmount(), payload.getPaymentMethod());
    }
}
