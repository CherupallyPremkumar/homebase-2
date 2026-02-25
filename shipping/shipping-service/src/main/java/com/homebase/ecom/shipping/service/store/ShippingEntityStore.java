package com.homebase.ecom.shipping.service.store;

import org.chenile.utils.entity.service.EntityStore;
import com.homebase.ecom.shipping.model.Shipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.chenile.base.exception.NotFoundException;
import com.homebase.ecom.shipping.configuration.dao.ShippingRepository;
import java.util.Optional;

public class ShippingEntityStore implements EntityStore<Shipping>{
    @Autowired private ShippingRepository shippingRepository;

	@Override
	public void store(Shipping entity) {
        shippingRepository.save(entity);
	}

	@Override
	public Shipping retrieve(String id) {
        Optional<Shipping> entity = shippingRepository.findById(id);
        if (entity.isPresent()) return entity.get();
        throw new NotFoundException(1500,"Unable to find Shipping with ID " + id);
	}

}
