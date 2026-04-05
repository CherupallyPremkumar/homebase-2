package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.dto.CompleteRefundOrderPayload;

/**
 * STM Action: Complete refund — transitions from REFUND_REQUESTED to REFUNDED.
 * Only SYSTEM can invoke (triggered by payment BC).
 */
public class CompleteRefundOrderAction extends AbstractSTMTransitionAction<Order, CompleteRefundOrderPayload> {

    @Override
    public void transitionTo(Order order, CompleteRefundOrderPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        if (payload != null && payload.getRefundId() != null) {
            order.getTransientMap().put("refundId", payload.getRefundId());
        }
        order.getTransientMap().previousPayload = payload;
    }
}
