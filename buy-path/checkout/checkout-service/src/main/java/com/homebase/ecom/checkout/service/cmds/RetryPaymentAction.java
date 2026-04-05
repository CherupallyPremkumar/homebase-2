package com.homebase.ecom.checkout.service.cmds;

import com.homebase.ecom.checkout.dto.RetryPaymentPayload;
import com.homebase.ecom.checkout.model.Checkout;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for 'retryPayment' event: PAYMENT_FAILED -> CHECK_RETRY_ALLOWED.
 * Increments the retry counter so the auto-state can enforce max retries.
 */
public class RetryPaymentAction extends AbstractSTMTransitionAction<Checkout, RetryPaymentPayload> {

    private static final Logger log = LoggerFactory.getLogger(RetryPaymentAction.class);

    @Override
    public void transitionTo(Checkout checkout, RetryPaymentPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        checkout.setPaymentRetryCount(checkout.getPaymentRetryCount() + 1);

        if (payload.paymentMethodId != null) {
            checkout.setPaymentMethodId(payload.paymentMethodId);
        }

        // Clear previous failure
        checkout.setFailureReason(null);

        log.info("[CHECKOUT] Retry payment #{} for checkoutId={}",
                checkout.getPaymentRetryCount(), checkout.getId());
    }
}
