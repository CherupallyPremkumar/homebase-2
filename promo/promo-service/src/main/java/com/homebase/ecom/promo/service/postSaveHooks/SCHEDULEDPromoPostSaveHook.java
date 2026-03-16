package com.homebase.ecom.promo.service.postSaveHooks;

import com.homebase.ecom.promo.model.Coupon;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post save hook for SCHEDULED state.
 * Logs that the promo has been scheduled and is awaiting activation.
 */
public class SCHEDULEDPromoPostSaveHook implements PostSaveHook<Coupon> {
    private static final Logger log = LoggerFactory.getLogger(SCHEDULEDPromoPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Coupon coupon, TransientMap map) {
        log.info("PromoScheduled: Promo '{}' (code={}) scheduled. Start: {}, End: {}",
                coupon.getName(), coupon.getCode(), coupon.getStartDate(), coupon.getEndDate());
    }
}
