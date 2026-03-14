package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.stm.action.STMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.service.validator.OrderPolicyValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * Entry-action for SHIPPED state.
 * Calculates estimated delivery date and sets delivery SLA timer.
 */
public class ShippedNotificationAction implements STMTransitionAction<Order> {

    private static final Logger log = LoggerFactory.getLogger(ShippedNotificationAction.class);

    /** Default estimated delivery days if not configured */
    private static final int DEFAULT_ESTIMATED_DELIVERY_DAYS = 5;

    @Autowired
    private OrderPolicyValidator policyValidator;

    @Override
    public void doTransition(Order order, Object transitionParam,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        int maxFulfillmentDays = policyValidator.getMaxFulfillmentDays();

        // Calculate estimated delivery date if not already set
        if (order.getDeliveryDate() == null) {
            order.setDeliveryDate(LocalDateTime.now().plusDays(DEFAULT_ESTIMATED_DELIVERY_DAYS));
        }

        // Set SLA based on max fulfillment days from ship date
        Date now = new Date();
        Calendar cal = Calendar.getInstance();

        // Yellow: at estimated delivery day
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_MONTH, DEFAULT_ESTIMATED_DELIVERY_DAYS);
        order.setSlaYellowDate(cal.getTime());

        // Red: max fulfillment days from ship date
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_MONTH, maxFulfillmentDays);
        order.setSlaRedDate(cal.getTime());

        log.info("Order shipped: orderId={}, estimatedDelivery={}, deliverySlaYellow={}, deliverySlaRed={}",
                order.getId(), order.getDeliveryDate(), order.getSlaYellowDate(), order.getSlaRedDate());
    }
}
