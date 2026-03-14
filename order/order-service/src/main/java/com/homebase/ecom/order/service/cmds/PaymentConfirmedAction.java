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
 * Entry-action for PAYMENT_CONFIRMED state.
 * Sets fulfillment SLA timer based on maxFulfillmentDays from order policy config.
 */
public class PaymentConfirmedAction implements STMTransitionAction<Order> {

    private static final Logger log = LoggerFactory.getLogger(PaymentConfirmedAction.class);

    @Autowired
    private OrderPolicyValidator policyValidator;

    @Override
    public void doTransition(Order order, Object transitionParam,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        int maxFulfillmentDays = policyValidator.getMaxFulfillmentDays();
        int slaAlertAfterHours = policyValidator.getSlaAlertAfterHours();

        Date now = new Date();
        Calendar cal = Calendar.getInstance();

        // Yellow SLA: alert after configured hours
        cal.setTime(now);
        cal.add(Calendar.HOUR_OF_DAY, slaAlertAfterHours);
        order.setSlaYellowDate(cal.getTime());

        // Red SLA: max fulfillment days
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_MONTH, maxFulfillmentDays);
        order.setSlaRedDate(cal.getTime());

        log.info("Payment confirmed for order: {}, fulfillment SLA set: yellowDate={}, redDate={} (maxDays={})",
                order.getId(), order.getSlaYellowDate(), order.getSlaRedDate(), maxFulfillmentDays);
    }
}
