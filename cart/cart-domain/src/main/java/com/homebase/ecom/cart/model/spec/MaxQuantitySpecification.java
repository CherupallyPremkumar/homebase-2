package com.homebase.ecom.cart.model.spec;

import com.homebase.ecom.cart.model.Cart;

/**
 * Specification to enforce the maximum quantity per item.
 */
public class MaxQuantitySpecification implements CartSpecification {
    private final int maxQuantity;
    private final int candidateQuantity;

    public MaxQuantitySpecification(int maxQuantity, int candidateQuantity) {
        this.maxQuantity = maxQuantity;
        this.candidateQuantity = candidateQuantity;
    }

    @Override
    public boolean isSatisfiedBy(Cart cart) {
        return candidateQuantity <= maxQuantity;
    }
}
