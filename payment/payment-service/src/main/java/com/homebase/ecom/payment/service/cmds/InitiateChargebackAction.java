package com.homebase.ecom.payment.service.cmds;

import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.dto.InitiateChargebackPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for initiating a chargeback dispute.
 */
public class InitiateChargebackAction extends AbstractSTMTransitionAction<Payment, InitiateChargebackPayload> {

    private static final Logger log = LoggerFactory.getLogger(InitiateChargebackAction.class);

    @Override
    public void transitionTo(Payment payment, InitiateChargebackPayload payload, State startState,
            String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        log.warn("Chargeback initiated for paymentId={}, orderId={}, chargebackId={}",
                payment.getId(), payment.getOrderId(),
                payload != null ? payload.getChargebackId() : "N/A");
    }
}
