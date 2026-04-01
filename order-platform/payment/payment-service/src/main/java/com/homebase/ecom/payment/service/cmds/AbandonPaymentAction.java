package com.homebase.ecom.payment.service.cmds;

import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.dto.AbandonPaymentPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for manually abandoning a failed payment.
 */
public class AbandonPaymentAction extends AbstractSTMTransitionAction<Payment, AbandonPaymentPayload> {

    private static final Logger log = LoggerFactory.getLogger(AbandonPaymentAction.class);

    @Override
    public void transitionTo(Payment payment, AbandonPaymentPayload payload, State startState,
            String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        log.info("Payment abandoned for paymentId={}, orderId={}", payment.getId(), payment.getOrderId());
    }
}
