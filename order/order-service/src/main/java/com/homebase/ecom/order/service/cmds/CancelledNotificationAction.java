package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.stm.action.STMTransitionAction;
import com.homebase.ecom.order.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entry-action for CANCELLED state.
 * Clears SLA timers and logs cancellation.
 */
public class CancelledNotificationAction implements STMTransitionAction<Order> {

    private static final Logger log = LoggerFactory.getLogger(CancelledNotificationAction.class);

    @Override
    public void doTransition(Order order, Object transitionParam,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Clear SLA timers — order is cancelled, no further processing expected
        order.setSlaYellowDate(null);
        order.setSlaRedDate(null);

        log.info("Order cancelled: orderId={}, customerId={}, totalAmount={}",
                order.getId(), order.getUser_Id(),
                order.getTotalAmount() != null ? order.getTotalAmount() : "N/A");
    }
}
