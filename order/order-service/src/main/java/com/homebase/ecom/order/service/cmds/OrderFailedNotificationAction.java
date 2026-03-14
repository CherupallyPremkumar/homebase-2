package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.stm.action.STMTransitionAction;
import com.homebase.ecom.order.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entry-action for FAILED state.
 * Logs the failure and clears SLA timers since order processing has stopped.
 * A notification to the customer would be sent via the notification service.
 */
public class OrderFailedNotificationAction implements STMTransitionAction<Order> {

    private static final Logger log = LoggerFactory.getLogger(OrderFailedNotificationAction.class);

    @Override
    public void doTransition(Order order, Object transitionParam,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Clear SLA timers since the order has failed
        order.setSlaYellowDate(null);
        order.setSlaRedDate(null);

        log.warn("Order payment FAILED: orderId={}, customerId={}, totalAmount={}",
                order.getId(), order.getUser_Id(),
                order.getTotalAmount() != null ? order.getTotalAmount() : "N/A");
    }
}
