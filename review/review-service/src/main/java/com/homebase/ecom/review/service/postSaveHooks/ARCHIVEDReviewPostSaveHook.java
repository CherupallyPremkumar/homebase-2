package com.homebase.ecom.review.service.postSaveHooks;

import com.homebase.ecom.review.model.Review;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post save hook for the ARCHIVED state.
 * Logs the archival event.
 */
public class ARCHIVEDReviewPostSaveHook implements PostSaveHook<Review> {

    private static final Logger log = LoggerFactory.getLogger(ARCHIVEDReviewPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Review review, TransientMap map) {
        log.info("Review {} archived from {}. Product: {}. Reason: {}",
                review.getId(), startState.getStateId(), review.getProductId(), review.getModeratorNotes());
    }
}
