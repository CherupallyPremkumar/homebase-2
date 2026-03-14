package com.homebase.ecom.promo.service.postSaveHooks;

import com.homebase.ecom.promo.model.Coupon;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;

/**
 * Contains customized post Save Hook for the DRAFT state.
 */
public class DRAFTPromoPostSaveHook implements PostSaveHook<Coupon> {
    @Override
    public void execute(State startState, State endState, Coupon coupon, TransientMap map) {
    }
}
