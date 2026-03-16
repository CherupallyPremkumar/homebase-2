package com.homebase.ecom.review.service.postSaveHooks;

import com.homebase.ecom.review.model.Review;
import com.homebase.ecom.review.domain.port.NotificationPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Post save hook for the UNDER_MODERATION state.
 * Notifies moderators that a new review is in the moderation queue.
 */
public class UNDER_MODERATIONReviewPostSaveHook implements PostSaveHook<Review> {

    private static final Logger log = LoggerFactory.getLogger(UNDER_MODERATIONReviewPostSaveHook.class);

    @Autowired(required = false)
    private NotificationPort notificationPort;

    @Override
    public void execute(State startState, State endState, Review review, TransientMap map) {
        log.info("Review {} entered UNDER_MODERATION from {}. Product: {}",
                review.getId(), startState.getStateId(), review.getProductId());

        if (notificationPort != null) {
            notificationPort.notifyModerators(review.getId(), review.getProductId(),
                    "New review awaiting moderation");
        }
    }
}
