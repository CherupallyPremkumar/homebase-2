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
 * STM action for 'paymentSuccess' event: AWAITING_PAYMENT → COMPLETED.
 * Records payment confirmation and tells cart to complete checkout.
 */
public class PaymentSuccessAction extends AbstractSTMTransitionAction<Checkout, PaymentResultPayload> {

    private static final Logger log = LoggerFactory.getLogger(PaymentSuccessAction.class);

    @Override
    public void transitionTo(Checkout checkout, PaymentResultPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (payload.paymentId != null) {
            checkout.setPaymentId(payload.paymentId);
        }

        log.info("[CHECKOUT] Payment success for checkoutId={}, paymentId={}, orderId={}",
                checkout.getId(), checkout.getPaymentId(), checkout.getOrderId());
    }
}
