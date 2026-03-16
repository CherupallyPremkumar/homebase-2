package com.homebase.ecom.promo.service.postSaveHooks;

import com.homebase.ecom.promo.model.Coupon;
import com.homebase.ecom.shared.event.KafkaTopics;
import org.chenile.pubsub.ChenilePub;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Item 13: PromoActivatedHook -- notify marketing, publish PROMO_ACTIVATED to promo.events.
 */
public class ACTIVEPromoPostSaveHook implements PostSaveHook<Coupon> {
    private static final Logger log = LoggerFactory.getLogger(ACTIVEPromoPostSaveHook.class);

    @Autowired(required = false)
    private ChenilePub chenilePub;

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

        // Publish to promo.events topic
        if (chenilePub != null) {
            try {
                String body = String.format(
                    "{\"eventType\":\"PROMO_ACTIVATED\",\"promoCode\":\"%s\",\"promoName\":\"%s\",\"discountType\":\"%s\",\"discountValue\":%s}",
                    coupon.getCode(), coupon.getName(), coupon.getDiscountType(), coupon.getDiscountValue());
                chenilePub.publish("promo.events", body,
                        java.util.Map.of("key", coupon.getCode(), "eventType", "PROMO_ACTIVATED"));
            } catch (Exception e) {
                log.error("Failed to publish PROMO_ACTIVATED event for code={}", coupon.getCode(), e);
            }
        }
    }
}
