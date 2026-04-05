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
 * STM action for marking payment as succeeded.
 * Records gateway transaction ID and response.
 */
public class SucceedPaymentAction extends AbstractSTMTransitionAction<Payment, ProcessPaymentPayload> {

    private static final Logger log = LoggerFactory.getLogger(SucceedPaymentAction.class);

    @Override
    public void transitionTo(Payment payment, ProcessPaymentPayload payload, State startState,
            String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        payment.recordSuccess(payload.getGatewayTransactionId(), payload.getGatewayResponse());

        log.info("Payment succeeded for paymentId={}, orderId={}, gatewayTxnId={}",
                payment.getId(), payment.getOrderId(), payload.getGatewayTransactionId());
    }
}
