package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.stm.action.STMTransitionAction;
import com.homebase.ecom.order.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

/**
 * Entry-action for CREATED state.
 * Sets an SLA timer indicating how long we wait for payment before
 * the order should be considered stale.
 */
public class OrderCreatedNotificationAction implements STMTransitionAction<Order> {

    private static final Logger log = LoggerFactory.getLogger(OrderCreatedNotificationAction.class);

    /** Hours allowed for payment before SLA yellow warning */
    private static final int PAYMENT_SLA_YELLOW_HOURS = 2;
    /** Hours allowed for payment before SLA red (expired) */
    private static final int PAYMENT_SLA_RED_HOURS = 4;

    @Override
    public void doTransition(Order order, Object transitionParam,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Set SLA timers for payment expectation
        Date now = new Date();
        Calendar cal = Calendar.getInstance();

        cal.setTime(now);
        cal.add(Calendar.HOUR_OF_DAY, PAYMENT_SLA_YELLOW_HOURS);
        order.setSlaYellowDate(cal.getTime());

        cal.setTime(now);
        cal.add(Calendar.HOUR_OF_DAY, PAYMENT_SLA_RED_HOURS);
        order.setSlaRedDate(cal.getTime());

        int itemCount = (order.getItems() != null) ? order.getItems().size() : 0;
        log.info("Order entering CREATED state: orderId={}, items={}, paymentSlaYellow={}, paymentSlaRed={}",
                order.getId(), itemCount, order.getSlaYellowDate(), order.getSlaRedDate());
    }
}
