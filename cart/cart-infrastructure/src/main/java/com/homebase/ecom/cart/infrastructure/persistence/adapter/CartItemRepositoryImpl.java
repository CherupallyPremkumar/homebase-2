package com.homebase.ecom.cart.infrastructure.persistence.adapter;

import com.homebase.ecom.cart.infrastructure.persistence.repository.CartItemJpaRepository;
import com.homebase.ecom.cart.model.CartItemStatus;
import com.homebase.ecom.cart.repository.CartItemRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CartItemRepositoryImpl implements CartItemRepository {
    private final CartItemJpaRepository jpaRepository;

    public CartItemRepositoryImpl(CartItemJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void updatePriceAndSeller(String productId, BigDecimal price, String currency, String sellerId, CartItemStatus status) {
        jpaRepository.updatePriceAndSeller(productId, price, currency, sellerId, status.name());
    }

    @Override
    public void updateStatusByProductId(String productId, CartItemStatus status) {
        jpaRepository.updateStatusByProductId(productId, status.name());
    }
}
