package com.homebase.ecom.payment.service.cmds;

import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.dto.RetryPaymentPayload;
import com.homebase.ecom.payment.service.validator.PaymentPolicyValidator;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * STM action for retrying a failed payment.
 * Increments retry count; the CHECK_RETRY auto-state evaluates
 * whether retryCount >= maxRetryAttempts to route to ABANDONED or RETRY.
 */
public class RetryPaymentAction extends AbstractSTMTransitionAction<Payment, RetryPaymentPayload> {

    private static final Logger log = LoggerFactory.getLogger(RetryPaymentAction.class);

    @Autowired
    private PaymentPolicyValidator policyValidator;

    @Override
    public void transitionTo(Payment payment, RetryPaymentPayload payload, State startState,
            String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Validate retry is allowed (belt-and-suspenders; auto-state also checks)
        policyValidator.validateRetryAllowed(payment);

        payment.incrementRetryCount();
        payment.setFailureReason(null); // Clear previous failure reason for fresh attempt

        log.info("Payment retry #{} for paymentId={}, orderId={}",
                payment.getRetryCount(), payment.getId(), payment.getOrderId());
    }
}
