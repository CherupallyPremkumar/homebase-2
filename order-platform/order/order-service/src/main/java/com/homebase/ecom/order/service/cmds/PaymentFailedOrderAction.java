package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.dto.PaymentFailedOrderPayload;

/**
 * STM Action: Handle payment failure — transitions to PAYMENT_FAILED.
 */
public class PaymentFailedOrderAction extends AbstractSTMTransitionAction<Order, PaymentFailedOrderPayload> {

    @Override
    public void transitionTo(Order order, PaymentFailedOrderPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        if (payload != null && payload.getErrorDetails() != null) {
            order.getTransientMap().put("paymentErrorDetails", payload.getErrorDetails());
        }
        order.getTransientMap().previousPayload = payload;
    }
}
