package com.homebase.ecom.checkout.service.cmds;

import com.homebase.ecom.checkout.dto.PaymentResultPayload;
import com.homebase.ecom.checkout.model.Checkout;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for 'paymentFailed' event: AWAITING_PAYMENT → PAYMENT_FAILED.
 * Records failure reason. Compensation should be triggered separately.
 */
public class PaymentFailedAction extends AbstractSTMTransitionAction<Checkout, PaymentResultPayload> {

    private static final Logger log = LoggerFactory.getLogger(PaymentFailedAction.class);

    @Override
    public void transitionTo(Checkout checkout, PaymentResultPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        checkout.setFailureReason(payload.failureReason != null ? payload.failureReason : "Payment failed");

        log.warn("[CHECKOUT] Payment failed for checkoutId={}, reason={}",
                checkout.getId(), checkout.getFailureReason());
    }
}
