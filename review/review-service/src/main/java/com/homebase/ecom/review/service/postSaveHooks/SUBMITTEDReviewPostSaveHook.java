package com.homebase.ecom.review.service.postSaveHooks;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.review.model.Review;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.ReviewSubmittedEvent;
import org.chenile.pubsub.ChenilePub;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Post save hook for the SUBMITTED state.
 * Publishes REVIEW_SUBMITTED event to review.events topic.
 */
public class SUBMITTEDReviewPostSaveHook implements PostSaveHook<Review> {

    private static final Logger log = LoggerFactory.getLogger(SUBMITTEDReviewPostSaveHook.class);

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Autowired(required = false)
    private ObjectMapper objectMapper;

    @Override
    public void execute(State startState, State endState, Review review, TransientMap map) {
        if (chenilePub == null || objectMapper == null) return;

        ReviewSubmittedEvent event = new ReviewSubmittedEvent(
                review.getId(), review.getProductId(), review.getCustomerId(),
                review.getOrderId(), review.getRating(), review.isVerifiedPurchase());

        try {
            String body = objectMapper.writeValueAsString(event);
            chenilePub.publish(KafkaTopics.REVIEW_EVENTS, body,
                    Map.of("key", review.getProductId(), "eventType", ReviewSubmittedEvent.EVENT_TYPE));
            log.info("Published REVIEW_SUBMITTED for review {} product {}", review.getId(), review.getProductId());
        } catch (JacksonException e) {
            log.error("Failed to serialize ReviewSubmittedEvent for review={}", review.getId(), e);
        }
    }
}
