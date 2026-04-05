package com.homebase.ecom.payment.service.cmds;

import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.dto.RequestAuthenticationPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for requesting 3DS/OTP authentication from gateway.
 */
public class RequestAuthenticationAction extends AbstractSTMTransitionAction<Payment, RequestAuthenticationPayload> {

    private static final Logger log = LoggerFactory.getLogger(RequestAuthenticationAction.class);

    @Override
    public void transitionTo(Payment payment, RequestAuthenticationPayload payload, State startState,
            String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        log.info("Authentication requested for paymentId={}, orderId={}, challengeType={}",
                payment.getId(), payment.getOrderId(),
                payload != null ? payload.getChallengeType() : "N/A");
    }
}
