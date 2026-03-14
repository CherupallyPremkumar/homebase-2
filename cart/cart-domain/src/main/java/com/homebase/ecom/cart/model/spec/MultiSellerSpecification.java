package com.homebase.ecom.cart.model.spec;

import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.model.CartItem;

/**
 * Specification to enforce multi-seller policies.
 */
public class MultiSellerSpecification implements CartSpecification {
    private final boolean allowed;
    private final String candidateSellerId;

    public MultiSellerSpecification(boolean allowed, String candidateSellerId) {
        this.allowed = allowed;
        this.candidateSellerId = candidateSellerId;
    }

    @Override
    public boolean isSatisfiedBy(Cart cart) {
        // If multi-seller is allowed, skip check
        if (allowed) {
            return true;
        }

        // If cart is empty, any seller is fine
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            return true;
        }

        // If candidate seller ID is null, we can't validate (might be a generic update)
        if (candidateSellerId == null) {
            return true;
        }

        // Ensure all existing items match the candidate seller ID
        return cart.getItems().stream()
                .allMatch(item -> candidateSellerId.equals(item.getSellerId()));
    }
}
