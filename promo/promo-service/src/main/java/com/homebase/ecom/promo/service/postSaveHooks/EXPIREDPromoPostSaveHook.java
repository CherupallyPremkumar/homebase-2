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
 * Item 13: PromoExpiredHook -- cleanup, publish PROMO_EXPIRED to promo.events.
 * Cart BC can remove any active applications of this promo.
 */
public class EXPIREDPromoPostSaveHook implements PostSaveHook<Coupon> {
    private static final Logger log = LoggerFactory.getLogger(EXPIREDPromoPostSaveHook.class);

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Override
    public void execute(State startState, State endState, Coupon coupon, TransientMap map) {
        log.info("PROMO_EXPIRED: Promo '{}' (code={}) has expired. Usage: {}/{}.",
                coupon.getName(), coupon.getCode(),
                coupon.getUsageCount(), coupon.getUsageLimit());

        map.put("eventType", "PROMO_EXPIRED");
        map.put("promoCode", coupon.getCode());

        // Publish to promo.events topic
        if (chenilePub != null) {
            try {
                String body = String.format(
                    "{\"eventType\":\"PROMO_EXPIRED\",\"promoCode\":\"%s\",\"promoName\":\"%s\",\"usageCount\":%d,\"usageLimit\":%d}",
                    coupon.getCode(), coupon.getName(),
                    coupon.getUsageCount() != null ? coupon.getUsageCount() : 0,
                    coupon.getUsageLimit() != null ? coupon.getUsageLimit() : 0);
                chenilePub.publish("promo.events", body,
                        java.util.Map.of("key", coupon.getCode(), "eventType", "PROMO_EXPIRED"));
            } catch (Exception e) {
                log.error("Failed to publish PROMO_EXPIRED event for code={}", coupon.getCode(), e);
            }
        }
    }
}
