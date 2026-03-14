package com.homebase.ecom.order.domain.port;

import com.homebase.ecom.order.model.Order;

import java.util.Optional;

public interface OrderRepository {
    Optional<Order> findById(String id);
    void save(Order order);
    void delete(String id);
}
