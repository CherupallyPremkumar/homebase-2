package com.homebase.ecom.promo.service.postSaveHooks;

import com.homebase.ecom.promo.model.Coupon;
import com.homebase.ecom.promo.port.PromoEventPublisherPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post-save hook for ACTIVE state.
 * Publishes PROMO_ACTIVATED event via domain port — notifies marketing and cart BC.
 */
public class ACTIVEPromoPostSaveHook implements PostSaveHook<Coupon> {
    private static final Logger log = LoggerFactory.getLogger(ACTIVEPromoPostSaveHook.class);

    private final PromoEventPublisherPort eventPublisher;

    public ACTIVEPromoPostSaveHook(PromoEventPublisherPort eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, Coupon coupon, TransientMap map) {
        log.info("PROMO_ACTIVATED: Promo '{}' (code={}) is now ACTIVE. " +
                "Discount: {} {}. Start: {}, End: {}. Usage: {}/{}",
                coupon.getName(), coupon.getCode(),
                coupon.getDiscountValue(), coupon.getDiscountType(),
                coupon.getStartDate(), coupon.getEndDate(),
                coupon.getUsageCount(), coupon.getUsageLimit());

        map.put("eventType", "PROMO_ACTIVATED");
        map.put("promoCode", coupon.getCode());
        map.put("previousState", startState.getStateId());

        eventPublisher.publishPromoActivated(coupon);
    }
}
