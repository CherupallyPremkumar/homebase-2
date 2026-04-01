package com.homebase.ecom.promo.service.postSaveHooks;

import com.homebase.ecom.promo.model.Coupon;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post save hook for ARCHIVED state. Terminal state, historical record.
 */
public class ARCHIVEDPromoPostSaveHook implements PostSaveHook<Coupon> {
    private static final Logger log = LoggerFactory.getLogger(ARCHIVEDPromoPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Coupon coupon, TransientMap map) {
        log.info("PromoArchived: Promo '{}' (code={}) archived from state {}.",
                coupon.getName(), coupon.getCode(), startState.getStateId());
    }
}
