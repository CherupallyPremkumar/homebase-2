package com.homebase.ecom.cart.repository;

import com.homebase.ecom.cart.model.CartItemStatus;
import java.math.BigDecimal;

/**
 * Domain port for cart item batch operations.
 * Infrastructure layer provides the JPA implementation.
 */
public interface CartItemRepository {
    void updatePriceAndSeller(String productId, BigDecimal price, String currency, String sellerId, CartItemStatus status);
    void updateStatusByProductId(String productId, CartItemStatus status);
}
