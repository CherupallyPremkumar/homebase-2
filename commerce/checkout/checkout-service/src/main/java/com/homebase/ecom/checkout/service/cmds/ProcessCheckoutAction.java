package com.homebase.ecom.checkout.service.cmds;

import com.homebase.ecom.checkout.model.Checkout;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * STM action for 'process' event: INITIATED → AWAITING_PAYMENT.
 * The actual saga work (7 steps) is delegated to OWIZ via
 * meta-orchestratedCommandsConfiguration in checkout-states.xml.
 *
 * This action runs AFTER the OWIZ chain completes successfully.
 * It sets the checkout expiration for payment timeout.
 */
public class ProcessCheckoutAction extends AbstractSTMTransitionAction<Checkout, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(ProcessCheckoutAction.class);
    private static final int PAYMENT_TIMEOUT_MINUTES = 30;

    @Override
    public void transitionTo(Checkout checkout, MinimalPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        checkout.setExpiresAt(LocalDateTime.now().plusMinutes(PAYMENT_TIMEOUT_MINUTES));

        log.info("[CHECKOUT] Process complete for checkoutId={}, orderId={}, paymentId={}, awaiting payment callback",
                checkout.getId(), checkout.getOrderId(), checkout.getPaymentId());
    }
}
