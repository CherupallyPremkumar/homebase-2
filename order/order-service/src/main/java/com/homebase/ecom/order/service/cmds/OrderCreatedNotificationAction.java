package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.State;
import org.chenile.stm.action.STMAction;
import com.homebase.ecom.order.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

/**
 * Entry-action for CREATED state.
 * Sets SLA timers for payment expectation.
 */
public class OrderCreatedNotificationAction implements STMAction<Order> {

    private static final Logger log = LoggerFactory.getLogger(OrderCreatedNotificationAction.class);
    private static final int PAYMENT_SLA_YELLOW_HOURS = 2;
    private static final int PAYMENT_SLA_RED_HOURS = 4;

    @Override
    public void execute(State fromState, State toState, Order order) throws Exception {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();

        cal.setTime(now);
        cal.add(Calendar.HOUR_OF_DAY, PAYMENT_SLA_YELLOW_HOURS);
        order.setSlaYellowDate(cal.getTime());

        cal.setTime(now);
        cal.add(Calendar.HOUR_OF_DAY, PAYMENT_SLA_RED_HOURS);
        order.setSlaRedDate(cal.getTime());

        int itemCount = (order.getItems() != null) ? order.getItems().size() : 0;
        log.info("Order entering CREATED state: orderId={}, items={}", order.getId(), itemCount);
    }
}
