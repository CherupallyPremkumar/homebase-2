package com.homebase.ecom.payment.service.cmds;

import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.dto.TimeoutPaymentPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for recording payment timeout.
 */
public class TimeoutPaymentAction extends AbstractSTMTransitionAction<Payment, TimeoutPaymentPayload> {

    private static final Logger log = LoggerFactory.getLogger(TimeoutPaymentAction.class);

    @Override
    public void transitionTo(Payment payment, TimeoutPaymentPayload payload, State startState,
            String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        payment.recordFailure("Payment timed out waiting for gateway response", null);
        log.warn("Payment timed out for paymentId={}, orderId={}",
                payment.getId(), payment.getOrderId());
    }
}
