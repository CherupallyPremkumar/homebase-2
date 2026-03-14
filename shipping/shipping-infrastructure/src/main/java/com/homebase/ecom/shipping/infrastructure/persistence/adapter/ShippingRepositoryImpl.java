package com.homebase.ecom.shipping.infrastructure.persistence.adapter;

import com.homebase.ecom.shipping.model.Shipping;
import com.homebase.ecom.shipping.domain.port.ShippingRepository;
import com.homebase.ecom.shipping.infrastructure.persistence.entity.ShippingEntity;
import com.homebase.ecom.shipping.infrastructure.persistence.mapper.ShippingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ShippingRepositoryImpl implements ShippingRepository {

    @Autowired
    private ShippingJpaRepository shippingJpaRepository;

    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public Optional<Shipping> findById(String id) {
        return shippingJpaRepository.findById(id).map(shippingMapper::toModel);
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
