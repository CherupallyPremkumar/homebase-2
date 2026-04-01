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
 * Cron job: finds expired carts and publishes expire events.
 *
 * Runs every hour. Queries cart-query for carts where expiresAt < now
 * in states ACTIVE, ABANDONED, or CHECKOUT_INITIATED.
 * Publishes {cartId, "expire"} to cart.events for each.
 */
public class CartExpirationJob {

    private static final Logger log = LoggerFactory.getLogger(CartExpirationJob.class);

    private final CartQueryPort cartQueryPort;
    private final ChenilePub chenilePub;

    public CartExpirationJob(CartQueryPort cartQueryPort, ChenilePub chenilePub) {
        this.cartQueryPort = cartQueryPort;
        this.chenilePub = chenilePub;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void run() {
        log.info("Cart expiration job started");

        List<String> expiredCartIds = cartQueryPort.findExpiredCarts();
        if (expiredCartIds.isEmpty()) {
            log.info("No expired carts found");
            return;
        }

        log.info("Found {} expired carts, publishing expire events", expiredCartIds.size());
        for (String cartId : expiredCartIds) {
            try {
                chenilePub.asyncPublish(KafkaTopics.CART_EVENTS, "{}",
                        Map.of("key", cartId,
                               "eventType", "expire",
                               "cartId", cartId,
                               "source", "cart-jobs-expiration"));
            } catch (Exception e) {
                log.error("Failed to publish expire event for cartId={}", cartId, e);
            }
        }

        log.info("Cart expiration job completed, published {} events", expiredCartIds.size());
    }
}
