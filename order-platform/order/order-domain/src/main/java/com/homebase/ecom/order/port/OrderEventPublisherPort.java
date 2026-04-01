package com.homebase.ecom.order.port;

import com.homebase.ecom.order.model.Order;

/**
 * Domain port for publishing order lifecycle events.
 * Infrastructure adapter handles serialization, topics, and delivery mechanism.
 */
public interface OrderEventPublisherPort {

    /**
     * Publishes ORDER_CREATED event when order enters CREATED state.
     * Triggers downstream BCs (inventory reservation, notification, etc.).
     */
    void publishOrderCreated(Order order);

    /**
     * Publishes ORDER_PAID event when payment succeeds.
     * Triggers fulfillment BC to start processing.
     */
    void publishOrderPaid(Order order);

    /**
     * Publishes ORDER_CANCELLED event when order is cancelled.
     * Triggers inventory release and payment refund.
     */
    void publishOrderCancelled(Order order);

    /**
     * Publishes ORDER_SHIPPED event when order is shipped.
     * Includes carrier and tracking information from transient map.
     */
    void publishOrderShipped(Order order, String carrier, String trackingNumber);

    /**
     * Publishes ORDER_DELIVERED event when order is delivered.
     * Notifies customer of delivery completion.
     */
    void publishOrderDelivered(Order order);

    /**
     * Publishes ORDER_COMPLETED event when order lifecycle completes.
     * Triggers settlement calculation.
     */
    void publishOrderCompleted(Order order);
}
