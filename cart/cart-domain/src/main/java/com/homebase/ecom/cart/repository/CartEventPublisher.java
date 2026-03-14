package com.homebase.ecom.cart.repository;

import com.homebase.ecom.shared.event.CartAbandonedEvent;
import com.homebase.ecom.shared.event.CartCheckoutCompletedEvent;
import com.homebase.ecom.shared.event.CartCheckoutInitiatedEvent;
import com.homebase.ecom.shared.event.CartCreatedEvent;
import com.homebase.ecom.shared.event.OrderCreatedEvent;

/**
 * Domain port for publishing cart-related events.
 */
public interface CartEventPublisher {
    void publishOrderCreated(OrderCreatedEvent event);

    void publishCartCheckoutCompleted(CartCheckoutCompletedEvent event);

    void publishCartCheckoutInitiated(CartCheckoutInitiatedEvent event);

    void publishCartAbandoned(CartAbandonedEvent event);

    void publishCartCreated(CartCreatedEvent event);
}
