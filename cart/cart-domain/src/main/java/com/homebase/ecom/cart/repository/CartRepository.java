package com.homebase.ecom.cart.repository;

import com.homebase.ecom.cart.model.Cart;
import java.util.Optional;

public interface CartRepository {
    Optional<Cart> findById(String id);

    Cart save(Cart cart);

    void deleteById(String id);

    Optional<Cart> findActiveCartByCustomerId(String customerId);
}
