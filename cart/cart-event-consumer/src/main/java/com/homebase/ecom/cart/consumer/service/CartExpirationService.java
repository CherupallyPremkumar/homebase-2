package com.homebase.ecom.cart.consumer.service;

import com.homebase.ecom.dto.CartTimeoutDto;
import com.homebase.ecom.cart.repository.CartDtoRepository;
import com.homebase.ecom.cart.service.CartService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service to handle cart expiration using decoupled proxies.
 */
@Service
public class CartExpirationService {

    private static final Logger log = LoggerFactory.getLogger(CartExpirationService.class);

    @Autowired
    private CartDtoRepository cartDtoRepository;

    @Autowired
    private CartService cartService;

    private static final String EXPIRE_EVENT = "expireCart";
    private static final String CHECKOUT_TIMEOUT_EVENT = "checkoutTimeout";
    private static final String WEBHOOK_TIMEOUT_EVENT = "webhookTimeout";

    /**
     * Identifies idle carts and triggers expiration via proxy.
     */
    public void expireIdleCarts(int daysIdle) {
        log.info("Checking for carts idle for more than {} days", daysIdle);
        Instant cutoff = Instant.now().minus(daysIdle, ChronoUnit.DAYS);
        List<CartTimeoutDto> idleCarts = cartDtoRepository.getIdleCarts(cutoff);
        log.info("Found {} idle carts to expire", idleCarts.size());

        for (CartTimeoutDto cart : idleCarts) {
            triggerEvent(cart.getId(), EXPIRE_EVENT);
        }
    }

    /**
     * Identifies carts stuck in checkout and triggers timeout.
     */
    public void handleStuckCheckouts(int minutesIdle) {
        log.info("Checking for carts stuck in checkout for more than {} minutes", minutesIdle);
        Instant cutoff = Instant.now().minus(minutesIdle, ChronoUnit.MINUTES);
        List<CartTimeoutDto> stuckCarts = cartDtoRepository.getStuckCheckouts(cutoff);
        for (CartTimeoutDto cart : stuckCarts) {
            triggerEvent(cart.getId(), CHECKOUT_TIMEOUT_EVENT);
        }
    }

    /**
     * Identifies carts stuck waiting for payment webhook and triggers timeout.
     */
    public void handleStuckPayments(int minutesIdle) {
        log.info("Checking for carts stuck in payment for more than {} minutes", minutesIdle);
        Instant cutoff = Instant.now().minus(minutesIdle, ChronoUnit.MINUTES);
        List<CartTimeoutDto> stuckCarts = cartDtoRepository.getStuckPayments(cutoff);
        for (CartTimeoutDto cart : stuckCarts) {
            triggerEvent(cart.getId(), WEBHOOK_TIMEOUT_EVENT);
        }
    }

    private void triggerEvent(String cartId, String event) {
        try {
            log.debug("Triggering event {} for cart: {}", event, cartId);
            cartService.proceedById(cartId, event, null);
        } catch (Exception e) {
            log.error("Failed to trigger event {} for cart: {}", event, cartId, e);
        }
    }
}
