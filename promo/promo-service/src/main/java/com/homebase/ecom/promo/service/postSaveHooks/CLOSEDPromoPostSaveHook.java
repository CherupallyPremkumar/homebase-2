package com.homebase.ecom.promo.service.postSaveHooks;

import com.homebase.ecom.promo.model.Coupon;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains customized post Save Hook for the CLOSED state.
 * Promo is permanently closed and can no longer be used.
 */
public class CLOSEDPromoPostSaveHook implements PostSaveHook<Coupon> {
    private static final Logger log = LoggerFactory.getLogger(CLOSEDPromoPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Coupon coupon, TransientMap map) {
        log.info("PromoClosedEvent: Promo code '{}' (id={}) permanently closed from state {}.",
                coupon.getCode(), coupon.getCouponId(), startState.getStateId());

        map.put("eventType", "PromoClosedEvent");
        map.put("promoCode", coupon.getCode());
        map.put("previousState", startState.getStateId());
    }
}
