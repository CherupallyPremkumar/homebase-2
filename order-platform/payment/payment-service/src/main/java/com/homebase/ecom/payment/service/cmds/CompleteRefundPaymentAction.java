package com.homebase.ecom.payment.service.cmds;

import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.dto.CompleteRefundPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * STM action for completing a refund.
 * Accumulates refundAmount so CHECK_REFUND_TYPE auto-state can determine
 * if this is a full or partial refund.
 */
public class CompleteRefundPaymentAction extends AbstractSTMTransitionAction<Payment, CompleteRefundPayload> {

    private static final Logger log = LoggerFactory.getLogger(CompleteRefundPaymentAction.class);

    @Override
    public void transitionTo(Payment payment, CompleteRefundPayload payload, State startState,
            String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (payload.getGatewayTransactionId() != null) {
            payment.setGatewayTransactionId(payload.getGatewayTransactionId());
        }
        if (payload.getGatewayResponse() != null) {
            payment.setGatewayResponse(payload.getGatewayResponse());
        }

        // Accumulate refund amount for partial refund support
        if (payload.getRefundedAmount() != null) {
            BigDecimal current = payment.getRefundAmount() != null ? payment.getRefundAmount() : BigDecimal.ZERO;
            payment.setRefundAmount(current.add(payload.getRefundedAmount()));
        }

        log.info("Refund completed for paymentId={}, orderId={}, totalRefunded={}, originalAmount={}",
                payment.getId(), payment.getOrderId(), payment.getRefundAmount(), payment.getAmount());
    }
}
