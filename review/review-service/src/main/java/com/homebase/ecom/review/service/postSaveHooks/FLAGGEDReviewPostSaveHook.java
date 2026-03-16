package com.homebase.ecom.review.service.postSaveHooks;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.review.model.Review;
import com.homebase.ecom.review.domain.port.NotificationPort;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.ReviewFlaggedEvent;
import org.chenile.pubsub.ChenilePub;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Post save hook for the FLAGGED state.
 * Publishes REVIEW_FLAGGED event and notifies moderators.
 */
public class FLAGGEDReviewPostSaveHook implements PostSaveHook<Review> {

    private static final Logger log = LoggerFactory.getLogger(FLAGGEDReviewPostSaveHook.class);

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Autowired(required = false)
    private ObjectMapper objectMapper;

    @Autowired(required = false)
    private NotificationPort notificationPort;

    @Override
    public void execute(State startState, State endState, Review review, TransientMap map) {
        // Notify moderators via port
        if (notificationPort != null) {
            notificationPort.notifyModerators(review.getId(), review.getProductId(), review.getModeratorNotes());
        }

        // Publish Kafka event
        if (chenilePub == null || objectMapper == null) {
            log.info("ReviewFlaggedEvent: Review {} flagged from {}. Reason: {} (Kafka not available)",
                    review.getId(), startState.getStateId(), review.getModeratorNotes());
            return;
        }

        ReviewFlaggedEvent event = new ReviewFlaggedEvent(
                review.getId(), review.getProductId(), review.getCustomerId(),
                review.getModeratorNotes(), startState.getStateId());

        try {
            String body = objectMapper.writeValueAsString(event);
            chenilePub.publish(KafkaTopics.REVIEW_EVENTS, body,
                    Map.of("key", review.getProductId(), "eventType", ReviewFlaggedEvent.EVENT_TYPE));
            log.info("Published REVIEW_FLAGGED for review {} product {}", review.getId(), review.getProductId());
        } catch (JacksonException e) {
            log.error("Failed to serialize ReviewFlaggedEvent for review={}", review.getId(), e);
        }
    }
}
