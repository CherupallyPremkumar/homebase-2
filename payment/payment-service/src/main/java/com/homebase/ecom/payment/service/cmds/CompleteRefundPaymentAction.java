package com.homebase.ecom.payment.service.cmds;

import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.dto.ProcessRefundPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for completing a refund after gateway confirmation.
 */
public class CompleteRefundPaymentAction extends AbstractSTMTransitionAction<Payment, ProcessRefundPayload> {

    private static final Logger log = LoggerFactory.getLogger(CompleteRefundPaymentAction.class);

    @Override
    public void transitionTo(Payment payment, ProcessRefundPayload payload, State startState,
            String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (payload.getGatewayTransactionId() != null) {
            payment.setGatewayTransactionId(payload.getGatewayTransactionId());
        }
        if (payload.getGatewayResponse() != null) {
            payment.setGatewayResponse(payload.getGatewayResponse());
        }

        log.info("Refund completed for paymentId={}, orderId={}", payment.getId(), payment.getOrderId());
    }
}
