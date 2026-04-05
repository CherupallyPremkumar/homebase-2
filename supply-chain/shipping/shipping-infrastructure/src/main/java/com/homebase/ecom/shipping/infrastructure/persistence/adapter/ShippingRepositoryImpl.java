package com.homebase.ecom.shipping.infrastructure.persistence.adapter;

import com.homebase.ecom.shipping.model.Shipping;
import com.homebase.ecom.shipping.domain.port.ShippingRepository;
import com.homebase.ecom.shipping.infrastructure.persistence.entity.ShippingEntity;
import com.homebase.ecom.shipping.infrastructure.persistence.mapper.ShippingMapper;

import java.util.Optional;

/**
 * Adapter implementing ShippingRepository port.
 * Wired as @Bean in ShippingConfiguration -- no @Repository annotation.
 */
public class ShippingRepositoryImpl implements ShippingRepository {

    private final ShippingJpaRepository shippingJpaRepository;
    private final ShippingMapper shippingMapper;

    public ShippingRepositoryImpl(ShippingJpaRepository shippingJpaRepository, ShippingMapper shippingMapper) {
        this.shippingJpaRepository = shippingJpaRepository;
        this.shippingMapper = shippingMapper;
    }

    @Override
    public Optional<Shipping> findById(String id) {
        return shippingJpaRepository.findById(id).map(shippingMapper::toModel);
    }

    @Override
    public Optional<Shipping> findByOrderId(String orderId) {
        return shippingJpaRepository.findByOrderId(orderId).map(shippingMapper::toModel);
    }

    @Override
    public void save(Shipping shipping) {
        ShippingEntity entity = shippingMapper.toEntity(shipping);
        shippingJpaRepository.save(entity);
    }

    @Override
    public void delete(String id) {
        shippingJpaRepository.deleteById(id);
    }
}
