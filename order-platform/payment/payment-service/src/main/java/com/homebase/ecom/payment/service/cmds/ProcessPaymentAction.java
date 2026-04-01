package com.homebase.ecom.payment.service.cmds;

import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.dto.ProcessPaymentPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for submitting payment to the gateway for processing.
 */
public class ProcessPaymentAction extends AbstractSTMTransitionAction<Payment, ProcessPaymentPayload> {

    private static final Logger log = LoggerFactory.getLogger(ProcessPaymentAction.class);

    @Override
    public void transitionTo(Payment payment, ProcessPaymentPayload payload, State startState,
            String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (payload.getGatewayTransactionId() != null) {
            payment.setGatewayTransactionId(payload.getGatewayTransactionId());
        }

        log.info("Payment processing started for paymentId={}, orderId={}",
                payment.getId(), payment.getOrderId());
    }
}
