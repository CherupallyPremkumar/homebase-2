package com.homebase.ecom.review.infrastructure.configuration;

import com.homebase.ecom.review.domain.port.ModerationPort;
import com.homebase.ecom.review.domain.port.NotificationPort;
import com.homebase.ecom.review.domain.port.ReviewEventPublisherPort;
import com.homebase.ecom.review.infrastructure.adapter.LoggingModerationAdapter;
import com.homebase.ecom.review.infrastructure.adapter.LoggingNotificationAdapter;
import com.homebase.ecom.review.infrastructure.integration.KafkaReviewEventPublisher;
import org.chenile.pubsub.ChenilePub;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

/**
 * Infrastructure layer: wires adapters to domain ports for the Review bounded context.
 *
 * Each adapter is a pure translator between domain ports and external infrastructure.
 * No @Component/@Service -- all beans declared explicitly via @Bean.
 */
@Configuration
public class ReviewInfrastructureConfiguration {

    @Bean("reviewModerationPort")
    ModerationPort reviewModerationPort() {
        return new LoggingModerationAdapter();
    }

    @Bean("reviewNotificationPort")
    NotificationPort reviewNotificationPort() {
        return new LoggingNotificationAdapter();
    }

    @Bean("reviewEventPublisherPort")
    ReviewEventPublisherPort reviewEventPublisherPort(ChenilePub chenilePub, ObjectMapper objectMapper) {
        return new KafkaReviewEventPublisher(chenilePub, objectMapper);
    }
}
