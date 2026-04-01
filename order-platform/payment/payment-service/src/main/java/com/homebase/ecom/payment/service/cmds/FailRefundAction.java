package com.homebase.ecom.payment.service.cmds;

import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.dto.FailRefundPayload;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for recording refund failure at gateway.
 */
public class FailRefundAction extends AbstractSTMTransitionAction<Payment, FailRefundPayload> {

    private static final Logger log = LoggerFactory.getLogger(FailRefundAction.class);

    @Override
    public void transitionTo(Payment payment, FailRefundPayload payload, State startState,
            String eventId, State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        log.warn("Refund failed for paymentId={}, orderId={}, reason={}",
                payment.getId(), payment.getOrderId(),
                payload != null ? payload.getFailureReason() : "N/A");
    }
}
