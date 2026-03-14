package com.homebase.ecom.shipping.infrastructure.persistence;

import org.chenile.utils.entity.service.EntityStore;
import com.homebase.ecom.shipping.model.Shipping;
import com.homebase.ecom.shipping.infrastructure.persistence.adapter.ShippingJpaRepository;
import com.homebase.ecom.shipping.infrastructure.persistence.mapper.ShippingMapper;
import org.chenile.base.exception.NotFoundException;

import java.util.Optional;

public class ChenileShippingEntityStore implements EntityStore<Shipping> {

    private final ShippingJpaRepository shippingJpaRepository;
    private final ShippingMapper shippingMapper;

    public ChenileShippingEntityStore(ShippingJpaRepository shippingJpaRepository, ShippingMapper shippingMapper) {
        this.shippingJpaRepository = shippingJpaRepository;
        this.shippingMapper = shippingMapper;
    }

    @Override
    public void store(Shipping entity) {
        shippingJpaRepository.save(shippingMapper.toEntity(entity));
    }

    @Override
    public Shipping retrieve(String id) {
        Optional<Shipping> entity = shippingJpaRepository.findById(id)
                .map(shippingMapper::toModel);
        if (entity.isPresent())
            return entity.get();
        throw new NotFoundException(1700, "Unable to find Shipping with ID " + id);
    }
}
