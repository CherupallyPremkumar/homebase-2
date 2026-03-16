package com.homebase.ecom.cart.infrastructure.persistence;

import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.infrastructure.persistence.entity.CartEntity;
import com.homebase.ecom.cart.infrastructure.persistence.repository.CartJpaRepository;
import com.homebase.ecom.cart.infrastructure.persistence.mapper.CartMapper;
import org.chenile.jpautils.store.ChenileJpaEntityStore;

public class ChenileCartEntityStore extends ChenileJpaEntityStore<Cart, CartEntity> {

    public ChenileCartEntityStore(CartJpaRepository repository, CartMapper mapper) {
        super(repository, (entity) -> mapper.toModel(entity), (model) -> mapper.toEntity(model),
                (existing, updated) -> mapper.mergeEntity(existing, updated));
    }
}
