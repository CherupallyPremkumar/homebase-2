package com.homebase.ecom.cart.model.spec;

import com.homebase.ecom.cart.model.Cart;

/**
 * Functional interface for Cart specifications (Domain Policies).
 */
@FunctionalInterface
public interface CartSpecification {
    /**
     * Checks if the specification is satisfied by the given cart.
     * 
     * @param cart The cart to validate
     * @return true if satisfied, false otherwise
     */
    boolean isSatisfiedBy(Cart cart);
}
