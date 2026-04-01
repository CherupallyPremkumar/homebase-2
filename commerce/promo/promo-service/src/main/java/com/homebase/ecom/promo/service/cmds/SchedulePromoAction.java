package com.homebase.ecom.promo.service.cmds;

import com.homebase.ecom.promo.dto.SchedulePromoPayload;
import com.homebase.ecom.promo.model.Coupon;
import com.homebase.ecom.promo.service.validator.PromoPolicyValidator;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Schedules a promo from DRAFT to SCHEDULED.
 * Validates discount limits, date validity, and usage limits via PromoPolicyValidator.
 */
public class SchedulePromoAction extends AbstractPromoAction<SchedulePromoPayload> {

    @Autowired
    private PromoPolicyValidator policyValidator;

    @Override
    public void transitionTo(Coupon coupon, SchedulePromoPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        // Item 3/4: Validate via policy validator
        policyValidator.validatePromoForScheduling(coupon);

        coupon.addActivity("schedule", "Promo scheduled for activation");
        super.transitionTo(coupon, payload, startState, eventId, endState, stm, transition);
    }
}
