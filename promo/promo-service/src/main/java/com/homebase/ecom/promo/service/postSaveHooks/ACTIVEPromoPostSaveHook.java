package com.homebase.ecom.promo.service.postSaveHooks;

import com.homebase.ecom.promo.model.Coupon;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains customized post Save Hook for the ACTIVE state.
 * Publishes PromoActivatedEvent so downstream systems (Cart, Checkout) can apply the promo.
 */
public class ACTIVEPromoPostSaveHook implements PostSaveHook<Coupon> {
    private static final Logger log = LoggerFactory.getLogger(ACTIVEPromoPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Coupon coupon, TransientMap map) {
        log.info("PromoActivatedEvent: Promo code '{}' (id={}) is now ACTIVE. " +
                "Discount: {} {}. Expiry: {}",
                coupon.getCode(), coupon.getCouponId(),
                coupon.getDiscountValue(), coupon.getDiscountType(),
                coupon.getExpiryDate());

        map.put("eventType", "PromoActivatedEvent");
        map.put("promoCode", coupon.getCode());
        map.put("previousState", startState.getStateId());
    }
}
