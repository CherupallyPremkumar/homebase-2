package com.homebase.ecom.notification.infrastructure.configuration;

import com.homebase.ecom.notification.domain.port.NotificationEventPublisherPort;
import com.homebase.ecom.notification.infrastructure.event.KafkaNotificationEventPublisher;
import org.chenile.pubsub.ChenilePub;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

/**
 * Infrastructure layer configuration for the Notification bounded context.
 * Wires adapter implementations to domain port interfaces.
 *
 * <p>All beans are module-prefixed to avoid collisions in the monolith.</p>
 */
@Configuration
public class NotificationInfrastructureConfiguration {

    @Bean("notificationEventPublisherPort")
    NotificationEventPublisherPort notificationEventPublisherPort(ChenilePub chenilePub, ObjectMapper objectMapper) {
        return new KafkaNotificationEventPublisher(chenilePub, objectMapper);
    }
}
