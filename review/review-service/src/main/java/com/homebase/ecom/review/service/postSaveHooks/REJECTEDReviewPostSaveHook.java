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
 * Post save hook for the REJECTED state.
 * Notifies the customer about the rejection with the reason.
 */
public class REJECTEDReviewPostSaveHook implements PostSaveHook<Review> {

    private static final Logger log = LoggerFactory.getLogger(REJECTEDReviewPostSaveHook.class);

    @Autowired(required = false)
    private NotificationPort notificationPort;

    @Override
    public void execute(State startState, State endState, Review review, TransientMap map) {
        log.info("Review {} rejected. Customer: {}. Reason: {}",
                review.getId(), review.getCustomerId(), review.getModeratorNotes());

        if (notificationPort != null) {
            notificationPort.notifyCustomer(review.getCustomerId(), review.getId(),
                    "REJECTED", review.getModeratorNotes());
        }
    }
}
