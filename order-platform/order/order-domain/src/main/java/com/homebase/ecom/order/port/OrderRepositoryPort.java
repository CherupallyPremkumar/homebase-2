package com.homebase.ecom.order.port;

import com.homebase.ecom.order.model.Order;
import java.util.List;
import java.util.Optional;

/**
 * Domain port for order persistence.
 * Infrastructure layer provides the adapter.
 */
public interface OrderRepositoryPort {
    Order save(Order order);
    Optional<Order> findById(String id);
    Optional<Order> findByOrderNumber(String orderNumber);
    List<Order> findByCustomerId(String customerId);
    List<Order> findAll();
    void deleteById(String id);
}
