package com.homebase.ecom.order.service;

import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.shared.event.CartCheckoutInitiatedEvent;

/**
 * Interface for order-related operations, to be called across modules.
 */
public interface OrderService {
    /**
     * Creates an order from a checkout initiation event.
     */
    Order createOrder(CartCheckoutInitiatedEvent event) throws Exception;

    /**
     * Retrieves an order by its ID.
     */
    Order getOrder(String orderId);

    /**
     * Updates an order.
     */
    void updateOrder(Order order);

    /**
     * Triggers a state transition for an order.
     */
    void proceed(String orderId, String eventId, Object payload) throws Exception;
}
