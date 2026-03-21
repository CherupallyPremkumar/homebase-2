package com.homebase.ecom.review.service.postSaveHooks;

import com.homebase.ecom.review.domain.port.NotificationPort;
import com.homebase.ecom.review.domain.port.ReviewEventPublisherPort;
import com.homebase.ecom.review.model.Review;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Post save hook for the FLAGGED state.
 * Publishes REVIEW_FLAGGED event via domain port and notifies moderators.
 */
public class FLAGGEDReviewPostSaveHook implements PostSaveHook<Review> {

    private static final Logger log = LoggerFactory.getLogger(FLAGGEDReviewPostSaveHook.class);

    @Autowired
    private ReviewEventPublisherPort reviewEventPublisherPort;

    @Autowired
    private NotificationPort notificationPort;

    @Override
    public void execute(State startState, State endState, Review review, TransientMap map) {
        log.info("ReviewFlagged: review {} from state {}. Reason: {}",
                review.getId(),
                startState != null ? startState.getStateId() : "null",
                review.getModeratorNotes());

        // Notify moderators via port
        if (notificationPort != null) {
            notificationPort.notifyModerators(review.getId(), review.getProductId(), review.getModeratorNotes());
        }

        // Publish Kafka event via domain port
        if (reviewEventPublisherPort != null) {
            String previousState = startState != null ? startState.getStateId() : "";
            reviewEventPublisherPort.publishReviewFlagged(review, previousState);
        }
    }
}
