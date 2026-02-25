package com.homebase.ecom.order.service.store;

import org.chenile.utils.entity.service.EntityStore;
import com.homebase.ecom.order.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.chenile.base.exception.NotFoundException;
import com.homebase.ecom.order.configuration.dao.OrderRepository;
import java.util.Optional;

public class OrderEntityStore implements EntityStore<Order>{
    @Autowired private OrderRepository orderRepository;

	@Override
	public void store(Order entity) {
        orderRepository.save(entity);
	}

	@Override
	public Order retrieve(String id) {
        Optional<Order> entity = orderRepository.findById(id);
        if (entity.isPresent()) return entity.get();
        throw new NotFoundException(1500,"Unable to find Order with ID " + id);
	}

}
