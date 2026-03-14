package com.homebase.ecom.review.service.postSaveHooks;

import com.homebase.ecom.review.model.Review;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains customized post Save Hook for the FLAGGED state.
 * Notifies admin that a review needs re-moderation.
 */
public class FLAGGEDReviewPostSaveHook implements PostSaveHook<Review> {
    private static final Logger log = LoggerFactory.getLogger(FLAGGEDReviewPostSaveHook.class);

	@Override
    public void execute(State startState, State endState, Review review, TransientMap map) {
        // Notify admin/moderation queue about the flagged review
        log.info("ReviewFlaggedEvent: Review {} flagged from state {}. Reason: {}. Needs re-moderation.",
                review.getId(), startState.getStateId(), review.description);

        map.put("eventType", "ReviewFlaggedEvent");
        map.put("previousState", startState.getStateId());
        map.put("flagReason", review.description);
    }
}
