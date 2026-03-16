package com.homebase.ecom.cart.domain.port;

import com.homebase.ecom.cart.domain.model.PricingResult;
import com.homebase.ecom.cart.model.Cart;

/**
 * Domain port for pricing calculations.
 *
 * Returns a domain-level PricingResult (not external DTOs — anti-corruption boundary).
 * The infrastructure adapter translates external PricingResponseDTO → PricingResult.
 * The service action (caller) stores the result on the Cart.
 */
public interface PricingPort {

    /**
     * Calls Pricing Service with a snapshot of the cart.
     * Returns the full pricing breakdown in domain terms.
     *
     * @throws IllegalStateException if pricing service returns an error (e.g. invalid coupon)
     */
    PricingResult calculatePricing(Cart cart);
}
