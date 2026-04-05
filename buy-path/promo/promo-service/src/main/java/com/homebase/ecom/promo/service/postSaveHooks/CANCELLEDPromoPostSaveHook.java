package com.homebase.ecom.promo.service.postSaveHooks;

import com.homebase.ecom.promo.model.Coupon;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post save hook for CANCELLED state.
 */
public class CANCELLEDPromoPostSaveHook implements PostSaveHook<Coupon> {
    private static final Logger log = LoggerFactory.getLogger(CANCELLEDPromoPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Coupon coupon, TransientMap map) {
        log.info("PromoCancelled: Promo '{}' (code={}) cancelled from state {}.",
                coupon.getName(), coupon.getCode(), startState.getStateId());
        map.put("eventType", "PROMO_CANCELLED");
        map.put("promoCode", coupon.getCode());
    }
}
