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
 * Entry-action for PICKED state.
 * Items are packed and ready for courier pickup.
 * Sets SLA timer for courier pickup (should happen within 24 hours).
 */
public class ItemsPackedAction implements STMTransitionAction<Order> {

    private static final Logger log = LoggerFactory.getLogger(ItemsPackedAction.class);

    /** Hours before courier pickup SLA yellow warning */
    private static final int PICKUP_SLA_YELLOW_HOURS = 12;
    /** Hours before courier pickup SLA red */
    private static final int PICKUP_SLA_RED_HOURS = 24;

    @Override
    public void doTransition(Order order, Object transitionParam,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        Date now = new Date();
        Calendar cal = Calendar.getInstance();

        cal.setTime(now);
        cal.add(Calendar.HOUR_OF_DAY, PICKUP_SLA_YELLOW_HOURS);
        order.setSlaYellowDate(cal.getTime());

        cal.setTime(now);
        cal.add(Calendar.HOUR_OF_DAY, PICKUP_SLA_RED_HOURS);
        order.setSlaRedDate(cal.getTime());

        int itemCount = (order.getItems() != null) ? order.getItems().size() : 0;
        log.info("Items packed for order: {}, itemCount={}, awaiting courier pickup. pickupSlaYellow={}, pickupSlaRed={}",
                order.getId(), itemCount, order.getSlaYellowDate(), order.getSlaRedDate());
    }
}
