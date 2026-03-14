package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.stm.action.STMTransitionAction;
import com.homebase.ecom.order.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entry-action for COMPLETED state.
 * Performs final cleanup: clears SLA timers since the order lifecycle is complete.
 */
public class OrderCompletedAction implements STMTransitionAction<Order> {

    private static final Logger log = LoggerFactory.getLogger(OrderCompletedAction.class);

    @Override
    public void doTransition(Order order, Object transitionParam,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Clear SLA timers — order lifecycle is complete
        order.setSlaYellowDate(null);
        order.setSlaRedDate(null);

        log.info("Order completed: orderId={}, customerId={}, totalAmount={}",
                order.getId(), order.getUser_Id(),
                order.getTotalAmount() != null ? order.getTotalAmount() : "N/A");
    }
}
