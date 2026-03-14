package com.homebase.ecom.cart.infrastructure.persistence;

import org.chenile.utils.entity.service.EntityStore;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.chenile.base.exception.NotFoundException;
import java.util.Optional;

public class ChenileCartEntityStore implements EntityStore<Cart> {
	@Autowired
	private CartRepository cartRepository;

	@Override
	public void store(Cart entity) {
		cartRepository.save(entity);
	}

	@Override
	public Cart retrieve(String id) {
		Optional<Cart> entity = cartRepository.findById(id);
		if (entity.isPresent())
			return entity.get();
		throw new NotFoundException(1500, "Unable to find Cart with ID " + id);
	}
}
