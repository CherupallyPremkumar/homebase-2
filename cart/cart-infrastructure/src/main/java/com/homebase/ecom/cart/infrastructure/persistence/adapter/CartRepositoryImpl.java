package com.homebase.ecom.cart.infrastructure.persistence.adapter;

import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.repository.CartRepository;
import com.homebase.ecom.cart.infrastructure.persistence.entity.CartEntity;
import com.homebase.ecom.cart.infrastructure.persistence.repository.CartJpaRepository;
import com.homebase.ecom.cart.infrastructure.persistence.mapper.CartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CartRepositoryImpl implements CartRepository {

    @Autowired
    private CartJpaRepository cartJpaRepository;

    @Autowired
    private CartMapper cartMapper;

    @Override
    public Optional<Cart> findById(String id) {
        return cartJpaRepository.findById(id).map(cartMapper::toModel);
    }

    @Override
    public Cart save(Cart cart) {
        CartEntity entity = cartMapper.toEntity(cart);
        CartEntity savedEntity = cartJpaRepository.save(entity);
        return cartMapper.toModel(savedEntity);
    }

    @Override
    public void deleteById(String id) {
        cartJpaRepository.deleteById(id);
    }
}
