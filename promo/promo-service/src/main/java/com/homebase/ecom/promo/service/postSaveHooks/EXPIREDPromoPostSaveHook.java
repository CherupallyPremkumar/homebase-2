package com.homebase.ecom.promo.service.postSaveHooks;

import com.homebase.ecom.promo.model.Coupon;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains customized post Save Hook for the EXPIRED state.
 * Publishes PromoExpiredEvent so Cart BC can remove any active applications of this promo.
 */
public class EXPIREDPromoPostSaveHook implements PostSaveHook<Coupon> {
    private static final Logger log = LoggerFactory.getLogger(EXPIREDPromoPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Coupon coupon, TransientMap map) {
        log.info("PromoExpiredEvent: Promo code '{}' (id={}) has expired. " +
                "Usage: {}/{}.",
                coupon.getCode(), coupon.getCouponId(),
                coupon.getCurrentUsage(), coupon.getMaxUsageCount());

        map.put("eventType", "PromoExpiredEvent");
        map.put("promoCode", coupon.getCode());
    }
}
