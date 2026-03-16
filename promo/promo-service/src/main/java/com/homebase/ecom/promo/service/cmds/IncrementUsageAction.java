package com.homebase.ecom.promo.service.cmds;

import com.homebase.ecom.promo.dto.IncrementUsagePayload;
import com.homebase.ecom.promo.model.Coupon;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

/**
 * Increments the usage count for a promo.
 * Triggered by ORDER_COMPLETED events via Kafka.
 * After incrementing, transitions to CHECK_USAGE auto-state
 * which decides if the promo should expire (usage limit reached).
 */
public class IncrementUsageAction extends AbstractPromoAction<IncrementUsagePayload> {
    @Override
    public void transitionTo(Coupon coupon, IncrementUsagePayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        coupon.incrementUsage();
        coupon.addActivity("incrementUsage",
                "Usage incremented to " + coupon.getUsageCount() + " for order " + payload.getOrderId());
        super.transitionTo(coupon, payload, startState, eventId, endState, stm, transition);
    }
}
