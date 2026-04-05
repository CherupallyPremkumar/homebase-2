package com.homebase.ecom.cart.jobs.scheduler;

import com.homebase.ecom.cart.jobs.query.CartQueryPort;
import com.homebase.ecom.shared.event.KafkaTopics;
import org.chenile.pubsub.ChenilePub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;

/**
 * Cron job: detects idle carts and publishes abandon events.
 *
 * Runs every 30 minutes. Queries cart-query for ACTIVE carts
 * with no activity for N hours (configurable threshold).
 * Publishes {cartId, "abandon"} to cart.events for each.
 */
public class CartAbandonmentJob {

    private static final Logger log = LoggerFactory.getLogger(CartAbandonmentJob.class);
    private static final int DEFAULT_IDLE_THRESHOLD_HOURS = 48;

    private final CartQueryPort cartQueryPort;
    private final ChenilePub chenilePub;

    public CartAbandonmentJob(CartQueryPort cartQueryPort, ChenilePub chenilePub) {
        this.cartQueryPort = cartQueryPort;
        this.chenilePub = chenilePub;
    }

    @Scheduled(cron = "0 */30 * * * *")
    public void run() {
        log.info("Cart abandonment job started");

        List<String> idleCartIds = cartQueryPort.findIdleCarts(DEFAULT_IDLE_THRESHOLD_HOURS);
        if (idleCartIds.isEmpty()) {
            log.info("No idle carts found");
            return;
        }

        log.info("Found {} idle carts, publishing abandon events", idleCartIds.size());
        for (String cartId : idleCartIds) {
            try {
                chenilePub.asyncPublish(KafkaTopics.CART_EVENTS, "{}",
                        Map.of("key", cartId,
                               "eventType", "abandon",
                               "cartId", cartId,
                               "source", "cart-jobs-abandonment"));
            } catch (Exception e) {
                log.error("Failed to publish abandon event for cartId={}", cartId, e);
            }
        }

        log.info("Cart abandonment job completed, published {} events", idleCartIds.size());
    }
}
