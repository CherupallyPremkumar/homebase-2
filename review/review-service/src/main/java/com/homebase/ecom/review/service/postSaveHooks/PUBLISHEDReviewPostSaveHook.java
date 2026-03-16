package com.homebase.ecom.review.service.postSaveHooks;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.review.model.Review;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.ReviewPublishedEvent;
import org.chenile.pubsub.ChenilePub;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Post save hook for the PUBLISHED state.
 * Publishes REVIEW_PUBLISHED event to review.events topic.
 * Product BC consumes this to update the product's average rating aggregate.
 */
public class PUBLISHEDReviewPostSaveHook implements PostSaveHook<Review> {

    private static final Logger log = LoggerFactory.getLogger(PUBLISHEDReviewPostSaveHook.class);

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Autowired(required = false)
    private ObjectMapper objectMapper;

    @Override
    public void execute(State startState, State endState, Review review, TransientMap map) {
        if (chenilePub == null || objectMapper == null) {
            log.info("ReviewPublishedEvent: Review {} for product {} published (Kafka not available). Rating: {}",
                    review.getId(), review.getProductId(), review.getRating());
            return;
        }

        ReviewPublishedEvent event = new ReviewPublishedEvent(
                review.getId(), review.getProductId(), review.getCustomerId(),
                review.getRating(), review.isVerifiedPurchase());

        try {
            String body = objectMapper.writeValueAsString(event);
            chenilePub.publish(KafkaTopics.REVIEW_EVENTS, body,
                    Map.of("key", review.getProductId(), "eventType", ReviewPublishedEvent.EVENT_TYPE));
            log.info("Published REVIEW_PUBLISHED for review {} product {} rating {}",
                    review.getId(), review.getProductId(), review.getRating());
        } catch (JacksonException e) {
            log.error("Failed to serialize ReviewPublishedEvent for review={}", review.getId(), e);
        }
    }
}
