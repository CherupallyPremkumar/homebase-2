package com.homebase.ecom.demoorder.port;

import com.homebase.ecom.demoorder.event.DemoOrderEvent;

/**
 * Port for publishing demo order domain events.
 * Implementations: InVM (EventProcessor) or Kafka (ChenilePub).
 */
public interface DemoOrderEventPublisherPort {
    void publish(DemoOrderEvent event);
}
