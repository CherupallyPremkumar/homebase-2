package com.homebase.ecom.payment.service.cmds;

import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.dto.CompleteAuthenticationPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for completing 3DS/OTP authentication.
 */
public class CompleteAuthenticationAction extends AbstractSTMTransitionAction<Payment, CompleteAuthenticationPayload> {

    private static final Logger log = LoggerFactory.getLogger(CompleteAuthenticationAction.class);

    @Override
    public void transitionTo(Payment payment, CompleteAuthenticationPayload payload, State startState,
            String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        log.info("Authentication completed for paymentId={}, orderId={}",
                payment.getId(), payment.getOrderId());
    }
}
