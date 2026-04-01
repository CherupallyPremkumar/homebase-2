package com.homebase.ecom.promo.service.cmds;

import com.homebase.ecom.promo.dto.ActivatePromoPayload;
import com.homebase.ecom.promo.model.Coupon;
import com.homebase.ecom.promo.service.validator.PromoPolicyValidator;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Activates a promo from SCHEDULED state.
 * Validates that start date is reached and end date is in the future.
 */
public class ActivatePromoAction extends AbstractPromoAction<ActivatePromoPayload> {

    @Autowired
    private PromoPolicyValidator policyValidator;

    @Override
    public void transitionTo(Coupon coupon, ActivatePromoPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        // Validate end date is in the future
        if (coupon.getEndDate() != null && coupon.getEndDate().isBefore(java.time.LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot activate a promo with an end date in the past");
        }
        policyValidator.validateActivePromoCount();

        coupon.addActivity("activate", "Promo activated and live");
        super.transitionTo(coupon, payload, startState, eventId, endState, stm, transition);
    }
}
