package com.homebase.ecom.promo.service.postSaveHooks;

import com.homebase.ecom.promo.model.Coupon;
import com.homebase.ecom.promo.port.PromoEventPublisherPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post-save hook for EXPIRED state.
 * Publishes PROMO_EXPIRED event via domain port — cart BC removes active applications.
 */
public class EXPIREDPromoPostSaveHook implements PostSaveHook<Coupon> {
    private static final Logger log = LoggerFactory.getLogger(EXPIREDPromoPostSaveHook.class);

    private final PromoEventPublisherPort eventPublisher;

    public EXPIREDPromoPostSaveHook(PromoEventPublisherPort eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, Coupon coupon, TransientMap map) {
        log.info("PROMO_EXPIRED: Promo '{}' (code={}) has expired. Usage: {}/{}.",
                coupon.getName(), coupon.getCode(),
                coupon.getUsageCount(), coupon.getUsageLimit());

        map.put("eventType", "PROMO_EXPIRED");
        map.put("promoCode", coupon.getCode());

        eventPublisher.publishPromoExpired(coupon);
    }
}
