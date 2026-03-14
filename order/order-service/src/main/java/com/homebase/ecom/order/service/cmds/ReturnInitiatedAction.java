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
 * Entry-action for RETURN_INITIATED state.
 * Sets SLA timer for return processing (admin must review within the window).
 */
public class ReturnInitiatedAction implements STMTransitionAction<Order> {

    private static final Logger log = LoggerFactory.getLogger(ReturnInitiatedAction.class);

    /** Hours before return review SLA yellow warning */
    private static final int RETURN_REVIEW_SLA_YELLOW_HOURS = 24;
    /** Hours before return review SLA red */
    private static final int RETURN_REVIEW_SLA_RED_HOURS = 72;

    @Override
    public void doTransition(Order order, Object transitionParam,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        Date now = new Date();
        Calendar cal = Calendar.getInstance();

        // Yellow: return review taking longer than expected
        cal.setTime(now);
        cal.add(Calendar.HOUR_OF_DAY, RETURN_REVIEW_SLA_YELLOW_HOURS);
        order.setSlaYellowDate(cal.getTime());

        // Red: return must be reviewed by this time
        cal.setTime(now);
        cal.add(Calendar.HOUR_OF_DAY, RETURN_REVIEW_SLA_RED_HOURS);
        order.setSlaRedDate(cal.getTime());

        log.info("Return initiated for order: {}, reviewSlaYellow={}, reviewSlaRed={}",
                order.getId(), order.getSlaYellowDate(), order.getSlaRedDate());
    }
}
