package com.homebase.ecom.cart.consumer.service;

import org.chenile.workflow.api.StateEntityService;
import com.homebase.ecom.cart.model.Cart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Service to handle cart expiration.
 * Uses STM processById to trigger expire events through the state machine,
 * ensuring all state transitions and post-save hooks execute correctly.
 *
 * Note: In the new architecture, cart expiration is also handled by the
 * auto-state CHECK_EXPIRATION in cart-states.xml (OGNL-based check on expiresAt).
 * This service provides scheduled batch expiration as a complementary mechanism.
 */
@Service
public class CartExpirationService {

    private static final Logger log = LoggerFactory.getLogger(CartExpirationService.class);

    @Autowired
    @Qualifier("_cartStateEntityService_")
    private StateEntityService<Cart> cartStateEntityService;

    private static final String EXPIRE_EVENT = "expire";

    /**
     * Triggers expiration for a specific cart via STM.
     */
    public void expireCart(String cartId) {
        try {
            log.debug("Triggering expire event for cart: {}", cartId);
            cartStateEntityService.processById(cartId, EXPIRE_EVENT, null);
        } catch (Exception e) {
            log.error("Failed to trigger expire event for cart: {}", cartId, e);
        }
    }
}
