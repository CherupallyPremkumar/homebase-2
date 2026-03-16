package com.homebase.ecom.cart.consumer.scheduler;

import com.homebase.ecom.cart.consumer.service.CartExpirationService;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Periodically triggers the cart expiration process.
 * Reads configuration from cconfig (cart.json) for expiration thresholds.
 */
@Component
public class CartExpirationScheduler {

    private static final Logger log = LoggerFactory.getLogger(CartExpirationScheduler.class);

    @Autowired
    private CartExpirationService cartExpirationService;

    @Autowired
    private org.chenile.cconfig.sdk.CconfigClient cconfigClient;

    private int getCartExpirationHours() {
        try {
            java.util.Map<String, Object> map = cconfigClient.value("cart", null);
            if (map != null) {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                JsonNode node = mapper.valueToTree(map);
                JsonNode expHours = node.at("/policies/expiration/cartExpirationHours");
                if (!expHours.isMissingNode()) {
                    return expHours.asInt();
                }
            }
        } catch (Exception e) {
            log.warn("Failed to fetch cart config from cconfig. Using default expiration hours.");
        }
        return 72; // default
    }

    private int getAbandonmentThresholdHours() {
        try {
            java.util.Map<String, Object> map = cconfigClient.value("cart", null);
            if (map != null) {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                JsonNode node = mapper.valueToTree(map);
                JsonNode abHours = node.at("/policies/expiration/abandonmentThresholdHours");
                if (!abHours.isMissingNode()) {
                    return abHours.asInt();
                }
            }
        } catch (Exception e) {
            log.warn("Failed to fetch cart config from cconfig. Using default abandonment hours.");
        }
        return 24; // default
    }

    /**
     * Runs every hour to check for carts that should be expired.
     * The actual expiration logic uses the auto-state CHECK_EXPIRATION in the STM,
     * but this scheduler provides a batch sweep for carts that haven't been accessed.
     */
    @Scheduled(cron = "0 0 * * * *")
    public void runCartExpiration() {
        int expirationHours = getCartExpirationHours();
        int abandonmentHours = getAbandonmentThresholdHours();

        log.info("Starting scheduled cart expiration job... Settings: expirationHours={}, abandonmentHours={}",
                expirationHours, abandonmentHours);
        try {
            // TODO: Query for ACTIVE carts with expiresAt < now and trigger expire event
            // This will be implemented when the cart query infrastructure is wired
            log.info("Completed scheduled cart expiration job.");
        } catch (Exception e) {
            log.error("Error during scheduled cart expiration job", e);
        }
    }
}
