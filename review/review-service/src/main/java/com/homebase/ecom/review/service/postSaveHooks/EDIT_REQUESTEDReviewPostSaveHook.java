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
 * Post save hook for the EDIT_REQUESTED state.
 * Notifies the customer that edits have been requested for their review.
 */
public class EDIT_REQUESTEDReviewPostSaveHook implements PostSaveHook<Review> {

    private static final Logger log = LoggerFactory.getLogger(EDIT_REQUESTEDReviewPostSaveHook.class);

    @Autowired(required = false)
    private NotificationPort notificationPort;

    @Override
    public void execute(State startState, State endState, Review review, TransientMap map) {
        log.info("Edit requested for review {}. Customer: {}. Instructions: {}",
                review.getId(), review.getCustomerId(), review.getModeratorNotes());

        if (notificationPort != null) {
            notificationPort.notifyCustomer(review.getCustomerId(), review.getId(),
                    "EDIT_REQUESTED", review.getModeratorNotes());
        }
    }
}
