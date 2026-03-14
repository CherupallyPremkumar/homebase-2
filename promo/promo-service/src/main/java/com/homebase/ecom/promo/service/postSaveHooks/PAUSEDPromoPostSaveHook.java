package com.homebase.ecom.promo.service.postSaveHooks;

import com.homebase.ecom.promo.model.Coupon;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains customized post Save Hook for the PAUSED state.
 * Notifies downstream that the promo is temporarily unavailable.
 */
public class PAUSEDPromoPostSaveHook implements PostSaveHook<Coupon> {
    private static final Logger log = LoggerFactory.getLogger(PAUSEDPromoPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Coupon coupon, TransientMap map) {
        log.info("PromoPausedEvent: Promo code '{}' (id={}) has been paused.",
                coupon.getCode(), coupon.getCouponId());

        map.put("eventType", "PromoPausedEvent");
        map.put("promoCode", coupon.getCode());
    }
}
