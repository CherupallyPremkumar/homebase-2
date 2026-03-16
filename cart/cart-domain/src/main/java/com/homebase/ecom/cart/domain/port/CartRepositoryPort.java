package com.homebase.ecom.cart.domain.port;

import com.homebase.ecom.cart.model.Cart;
import java.util.Optional;

/**
 * Domain port for cart persistence.
 */
public interface CartRepositoryPort {
    Optional<Cart> findById(String id);
    Cart save(Cart cart);
    void deleteById(String id);
    Optional<Cart> findActiveCartByCustomerId(String customerId);
}
