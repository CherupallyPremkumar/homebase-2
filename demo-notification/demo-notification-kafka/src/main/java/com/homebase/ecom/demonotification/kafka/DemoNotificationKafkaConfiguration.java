package com.homebase.ecom.demonotification.kafka;

import org.springframework.context.annotation.Configuration;

/**
 * Kafka consumer configuration for demo-notification.
 * When this module is on the classpath, Chenile's CustomKafkaConsumer automatically
 * subscribes to topics declared in @EventsSubscribedTo on the controller.
 * No explicit consumer wiring needed -- Chenile handles it via PubSubEntryPoint.
 */
@Configuration
public class DemoNotificationKafkaConfiguration {
    // Chenile auto-discovers @EventsSubscribedTo({"demo-order.events"}) on the controller
    // and creates a Kafka consumer for the "demo-order.events" topic.
}
