package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.stm.action.STMTransitionAction;
import com.homebase.ecom.order.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * Entry-action for DELIVERED state.
 * Records the actual delivery time and starts the return window timer.
 * Customer can initiate a return within the return window (default 10 days).
 */
public class DeliveredNotificationAction implements STMTransitionAction<Order> {

    private static final Logger log = LoggerFactory.getLogger(DeliveredNotificationAction.class);

    /** Days for the return window SLA */
    private static final int RETURN_WINDOW_DAYS = 10;
    /** Days for auto-completion if customer doesn't confirm */
    private static final int AUTO_COMPLETE_DAYS = 7;

    @Override
    public void doTransition(Order order, Object transitionParam,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Record actual delivery time
        order.setDeliveryDate(LocalDateTime.now());

        // Set SLA for customer to confirm delivery or initiate return
        Date now = new Date();
        Calendar cal = Calendar.getInstance();

        // Yellow: auto-complete warning
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_MONTH, AUTO_COMPLETE_DAYS);
        order.setSlaYellowDate(cal.getTime());

        // Red: return window closing
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_MONTH, RETURN_WINDOW_DAYS);
        order.setSlaRedDate(cal.getTime());

        log.info("Order delivered: orderId={}, deliveredAt={}, returnWindowCloses={}, autoCompleteBy={}",
                order.getId(), order.getDeliveryDate(), order.getSlaRedDate(), order.getSlaYellowDate());
    }
}
