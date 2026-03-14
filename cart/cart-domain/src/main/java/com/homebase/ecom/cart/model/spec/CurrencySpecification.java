package com.homebase.ecom.cart.model.spec;

import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.model.CartItem;

/**
 * Specification to enforce currency consistency in the cart.
 */
public class CurrencySpecification implements CartSpecification {
    private final boolean enforceSingle;
    private final String baseCurrency;
    private final String candidateCurrency;

    public CurrencySpecification(boolean enforceSingle, String baseCurrency, String candidateCurrency) {
        this.enforceSingle = enforceSingle;
        this.baseCurrency = baseCurrency;
        this.candidateCurrency = candidateCurrency;
    }

    @Override
    public boolean isSatisfiedBy(Cart cart) {
        if (!enforceSingle) {
            return true;
        }

        // 1. If base currency is fixed (e.g. via config), candidate must match it
        if (baseCurrency != null && !baseCurrency.trim().isEmpty()) {
            return baseCurrency.equalsIgnoreCase(candidateCurrency);
        }

        // 2. If cart already has items, they must all match the candidate currency
        if (cart.getItems() != null && !cart.getItems().isEmpty()) {
            for (CartItem item : cart.getItems()) {
                if (item.getPrice() != null && item.getPrice().getCurrency() != null) {
                    if (!candidateCurrency.equalsIgnoreCase(item.getPrice().getCurrency())) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
