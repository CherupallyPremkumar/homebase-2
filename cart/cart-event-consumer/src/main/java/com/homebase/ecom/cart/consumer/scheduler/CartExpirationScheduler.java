package com.homebase.ecom.cart.consumer.scheduler;

import com.homebase.ecom.cart.consumer.service.CartExpirationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Periodically triggers the cart expiration process.
 */
@Component
public class CartExpirationScheduler {

    private static final Logger log = LoggerFactory.getLogger(CartExpirationScheduler.class);

    @Autowired
    private CartExpirationService cartExpirationService;

    // Since cart config is centrally managed via cconfig, we inject CconfigClient
    @Autowired
    private org.chenile.cconfig.sdk.CconfigClient cconfigClient;

    /**
     * Helper to retrieve the current active Cart configuration from cconfig.
     */
    private com.homebase.ecom.cart.configuration.CartConfig getCartConfig() {
        try {
            java.util.Map<String, Object> map = cconfigClient.value("cart", null);
            if (map != null) {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                        false);
                return mapper.convertValue(map, com.homebase.ecom.cart.configuration.CartConfig.class);
            }
        } catch (Exception e) {
            log.warn("Failed to fetch config for cart expiration. Using defaults.");
        }
        return new com.homebase.ecom.cart.configuration.CartConfig();
    }

    /**
     * Runs every hour to check for carts that have been idle.
     * Cron expression: "0 0 * * * *" (At second 0, minute 0 of every hour)
     */
    @Scheduled(cron = "0 0 * * * *")
    public void runCartExpiration() {
        com.homebase.ecom.cart.configuration.CartConfig config = getCartConfig();
        int idleCartExpirationDays = config.getPolicies().getExpiration().getIdleDays();
        int stuckCheckoutMinutes = config.getPolicies().getExpiration().getStuckCheckoutMinutes();
        int stuckPaymentMinutes = config.getPolicies().getExpiration().getStuckPaymentMinutes();

        log.info(
                "Starting scheduled cart expiration job... Settings: idleDays={}, stuckCheckoutMin={}, stuckPaymentMin={}",
                idleCartExpirationDays, stuckCheckoutMinutes, stuckPaymentMinutes);
        try {
            cartExpirationService.expireIdleCarts(idleCartExpirationDays);

            // Check for checkouts stuck
            cartExpirationService.handleStuckCheckouts(stuckCheckoutMinutes);

            // Check for webhooks stuck
            cartExpirationService.handleStuckPayments(stuckPaymentMinutes);

            log.info("Completed scheduled cart expiration job.");
        } catch (Exception e) {
            log.error("Error during scheduled cart expiration job", e);
        }
    }
}
