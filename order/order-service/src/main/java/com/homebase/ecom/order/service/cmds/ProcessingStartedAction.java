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

import java.util.Calendar;
import java.util.Date;

/**
 * Entry-action for PROCESSING state.
 * Sets SLA alert timer for warehouse processing based on slaAlertAfterHours config.
 */
public class ProcessingStartedAction implements STMTransitionAction<Order> {

    private static final Logger log = LoggerFactory.getLogger(ProcessingStartedAction.class);

    @Autowired
    private OrderPolicyValidator policyValidator;

    @Override
    public void doTransition(Order order, Object transitionParam,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        int slaAlertAfterHours = policyValidator.getSlaAlertAfterHours();

        Date now = new Date();
        Calendar cal = Calendar.getInstance();

        // Yellow SLA: processing taking too long
        cal.setTime(now);
        cal.add(Calendar.HOUR_OF_DAY, slaAlertAfterHours);
        order.setSlaYellowDate(cal.getTime());

        // Red SLA: double the alert time
        cal.setTime(now);
        cal.add(Calendar.HOUR_OF_DAY, slaAlertAfterHours * 2);
        order.setSlaRedDate(cal.getTime());

        log.info("Processing started for order: {}, slaAlertAfterHours={}, yellowDate={}, redDate={}",
                order.getId(), slaAlertAfterHours, order.getSlaYellowDate(), order.getSlaRedDate());
    }
}
