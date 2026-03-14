package com.homebase.ecom.cart.model.spec;

import com.homebase.ecom.cart.model.Cart;

/**
 * Specification to enforce the maximum unique items per cart.
 */
public class MaxItemsSpecification implements CartSpecification {
    private final int maxItems;
    private final String candidateProductId;

    public MaxItemsSpecification(int maxItems, String candidateProductId) {
        this.maxItems = maxItems;
        this.candidateProductId = candidateProductId;
    }

    @Override
    public boolean isSatisfiedBy(Cart cart) {
        if (cart.getItems() == null) {
            return true;
        }

        long currentUniqueItems = cart.getItems().stream()
                .filter(item -> !item.getProductId().equals(candidateProductId))
                .count();

        return (currentUniqueItems + 1) <= maxItems;
    }
}
