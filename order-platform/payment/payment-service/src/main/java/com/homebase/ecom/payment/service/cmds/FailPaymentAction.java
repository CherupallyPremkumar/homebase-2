package com.homebase.ecom.payment.service.cmds;

import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.dto.FailPaymentPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for recording payment failure.
 */
public class FailPaymentAction extends AbstractSTMTransitionAction<Payment, FailPaymentPayload> {

    private static final Logger log = LoggerFactory.getLogger(FailPaymentAction.class);

    @Override
    public void transitionTo(Payment payment, FailPaymentPayload payload, State startState,
            String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        payment.recordFailure(payload.getFailureReason(), payload.getGatewayResponse());

        log.warn("Payment failed for paymentId={}, orderId={}, reason={}",
                payment.getId(), payment.getOrderId(), payload.getFailureReason());
    }
}
