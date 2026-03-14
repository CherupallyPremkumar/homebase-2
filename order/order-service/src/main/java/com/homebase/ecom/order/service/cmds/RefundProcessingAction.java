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
 * Entry-action for REFUND_INITIATED state.
 * Sets SLA timer for refund completion (payment gateway should process within window).
 */
public class RefundProcessingAction implements STMTransitionAction<Order> {

    private static final Logger log = LoggerFactory.getLogger(RefundProcessingAction.class);

    /** Hours before refund SLA yellow warning */
    private static final int REFUND_SLA_YELLOW_HOURS = 48;
    /** Hours before refund SLA red */
    private static final int REFUND_SLA_RED_HOURS = 120; // 5 days

    @Override
    public void doTransition(Order order, Object transitionParam,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        Date now = new Date();
        Calendar cal = Calendar.getInstance();

        // Yellow: refund processing taking longer than expected
        cal.setTime(now);
        cal.add(Calendar.HOUR_OF_DAY, REFUND_SLA_YELLOW_HOURS);
        order.setSlaYellowDate(cal.getTime());

        // Red: refund must be completed by this time
        cal.setTime(now);
        cal.add(Calendar.HOUR_OF_DAY, REFUND_SLA_RED_HOURS);
        order.setSlaRedDate(cal.getTime());

        log.info("Refund processing started for order: {}, refundSlaYellow={}, refundSlaRed={}",
                order.getId(), order.getSlaYellowDate(), order.getSlaRedDate());
    }
}
