package com.homebase.ecom.review.service.postSaveHooks;

import com.homebase.ecom.review.model.Review;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains customized post Save Hook for the REJECTED state.
 * Notifies the reviewer about the rejection with the reason.
 */
public class REJECTEDReviewPostSaveHook implements PostSaveHook<Review> {
    private static final Logger log = LoggerFactory.getLogger(REJECTEDReviewPostSaveHook.class);

	@Override
    public void execute(State startState, State endState, Review review, TransientMap map) {
        // Notify the reviewer that their review was rejected
        log.info("ReviewRejectedEvent: Review {} by user {} rejected. Reason: {}",
                review.getId(), review.getUserId(), review.description);

        map.put("eventType", "ReviewRejectedEvent");
        map.put("userId", review.getUserId());
        map.put("rejectionReason", review.description);
    }
}
