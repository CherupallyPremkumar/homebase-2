package com.homebase.ecom.checkout.domain.port;

import com.homebase.ecom.checkout.event.CheckoutEvent;

/**
 * Domain port for publishing checkout lifecycle events.
 * Single method — adapters are dumb pipes that serialize and send to "checkout.events" topic.
 */
public interface CheckoutEventPublisherPort {

    /**
     * Publishes a checkout domain event.
     * The adapter handles serialization and transport (InVM or Kafka).
     */
    void publish(CheckoutEvent event);
}
