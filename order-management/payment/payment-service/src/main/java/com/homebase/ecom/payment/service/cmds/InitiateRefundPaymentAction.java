package com.homebase.ecom.payment.service.cmds;

import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.dto.InitiateRefundPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for initiating a refund on a succeeded/settled payment.
 */
public class InitiateRefundPaymentAction extends AbstractSTMTransitionAction<Payment, InitiateRefundPayload> {

    private static final Logger log = LoggerFactory.getLogger(InitiateRefundPaymentAction.class);

    @Override
    public void transitionTo(Payment payment, InitiateRefundPayload payload, State startState,
            String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        log.info("Refund initiated for paymentId={}, orderId={}, reason={}",
                payment.getId(), payment.getOrderId(), payload.getRefundReason());
    }
}
