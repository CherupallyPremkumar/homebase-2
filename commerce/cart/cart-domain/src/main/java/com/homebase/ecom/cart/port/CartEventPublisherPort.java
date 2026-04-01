package com.homebase.ecom.cart.port;

import com.homebase.ecom.cart.event.CartEvent;

/**
 * Domain port for publishing cart lifecycle events.
 * Single method — adapters are dumb pipes that serialize and send to "cart.events" topic.
 */
public interface CartEventPublisherPort {

    /**
     * Publishes a cart domain event.
     * The adapter handles serialization and transport (InVM or Kafka).
     */
    void publish(CartEvent event);
}
