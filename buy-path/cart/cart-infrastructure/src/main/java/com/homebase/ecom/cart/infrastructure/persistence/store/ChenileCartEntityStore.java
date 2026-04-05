package com.homebase.ecom.cart.infrastructure.persistence.store;

import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.repository.CartRepository;
import com.homebase.ecom.cart.infrastructure.persistence.entity.CartEntity;
import com.homebase.ecom.cart.infrastructure.persistence.repository.CartJpaRepository;
import com.homebase.ecom.cart.infrastructure.persistence.mapper.CartMapper;
import com.homebase.ecom.core.jpa.ChenileJpaEntityStore;

import java.util.Optional;

public class ChenileCartEntityStore extends ChenileJpaEntityStore<Cart, CartEntity> implements CartRepository {

    private final CartJpaRepository cartJpaRepository;
    private final CartMapper cartMapper;

    public ChenileCartEntityStore(CartJpaRepository repository, CartMapper mapper) {
        super(repository, mapper::toModel, mapper::toEntity, mapper::mergeEntity);
        this.cartJpaRepository = repository;
        this.cartMapper = mapper;
    }

    @Override
    public Optional<Cart> findById(String id) {
        return Optional.ofNullable(retrieve(id));
    }

    @Override
    public Cart save(Cart cart) {
        store(cart);
        return cart;
    }

    @Override
    public void deleteById(String id) {
        cartJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Cart> findActiveCartByCustomerId(String customerId) {
        return cartJpaRepository.findByCustomerIdAndState_StateId(customerId, "ACTIVE")
                .map(cartMapper::toModel);
    }
}
