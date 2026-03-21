package com.homebase.ecom.payment.service.cmds;

import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.dto.InitiateCodPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for marking payment as Cash on Delivery.
 * INITIATED → COD_PENDING: no gateway involved, payment collected at delivery.
 */
public class InitiateCodAction extends AbstractSTMTransitionAction<Payment, InitiateCodPayload> {

    private static final Logger log = LoggerFactory.getLogger(InitiateCodAction.class);

    @Override
    public void transitionTo(Payment payment, InitiateCodPayload payload, State startState,
            String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        payment.setGatewayName("COD");

        log.info("Payment marked as COD for paymentId={}, orderId={}, amount={}",
                payment.getId(), payment.getOrderId(), payment.getAmount());
    }
}
