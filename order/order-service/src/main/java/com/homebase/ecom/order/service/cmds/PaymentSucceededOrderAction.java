package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.dto.PaymentSucceededOrderPayload;

/**
 * STM Action: Payment succeeded — transitions to PAID.
 */
public class PaymentSucceededOrderAction extends AbstractSTMTransitionAction<Order, PaymentSucceededOrderPayload> {

    @Override
    public void transitionTo(Order order, PaymentSucceededOrderPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        if (payload != null && payload.getPaymentId() != null) {
            order.getTransientMap().put("paymentId", payload.getPaymentId());
        }
        order.getTransientMap().previousPayload = payload;
    }
}
