package com.homebase.ecom.review.infrastructure.integration;

import com.homebase.ecom.review.domain.port.ReviewEventPublisherPort;
import com.homebase.ecom.review.model.Review;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.ReviewFlaggedEvent;
import com.homebase.ecom.shared.event.ReviewPublishedEvent;
import com.homebase.ecom.shared.event.ReviewSubmittedEvent;
import org.chenile.pubsub.ChenilePub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * Infrastructure adapter for publishing review domain events to Kafka.
 * Uses ChenilePub for async event delivery to review.events topic.
 * No @Component -- wired explicitly via @Bean in ReviewInfrastructureConfiguration.
 */
public class KafkaReviewEventPublisher implements ReviewEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaReviewEventPublisher.class);

    private final ChenilePub chenilePub;
    private final ObjectMapper objectMapper;

    public KafkaReviewEventPublisher(ChenilePub chenilePub, ObjectMapper objectMapper) {
        this.chenilePub = chenilePub;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publishReviewSubmitted(Review review) {
        ReviewSubmittedEvent event = new ReviewSubmittedEvent(
                review.getId(), review.getProductId(), review.getCustomerId(),
                review.getOrderId(), review.getRating(), review.isVerifiedPurchase());

        publish(review.getProductId(), event, ReviewSubmittedEvent.EVENT_TYPE,
                "review " + review.getId());
    }

    @Override
    public void publishReviewPublished(Review review) {
        ReviewPublishedEvent event = new ReviewPublishedEvent(
                review.getId(), review.getProductId(), review.getCustomerId(),
                review.getRating(), review.isVerifiedPurchase());

        publish(review.getProductId(), event, ReviewPublishedEvent.EVENT_TYPE,
                "review " + review.getId());
    }

    @Override
    public void publishReviewFlagged(Review review, String previousState) {
        ReviewFlaggedEvent event = new ReviewFlaggedEvent(
                review.getId(), review.getProductId(), review.getCustomerId(),
                review.getModeratorNotes(), previousState);

        publish(review.getProductId(), event, ReviewFlaggedEvent.EVENT_TYPE,
                "review " + review.getId());
    }

    private void publish(String key, Object event, String eventType, String entityDesc) {
        try {
            String body = objectMapper.writeValueAsString(event);
            chenilePub.publish(KafkaTopics.REVIEW_EVENTS, body,
                    Map.of("key", key, "eventType", eventType));
            log.info("Published {} for {}", eventType, entityDesc);
        } catch (JacksonException e) {
            log.error("Failed to serialize {} for {}", eventType, entityDesc, e);
        }
    }
}
