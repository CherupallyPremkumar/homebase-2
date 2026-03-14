package com.homebase.ecom.review.service.postSaveHooks;

import com.homebase.ecom.review.model.Review;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;

/**
 * Contains customized post Save Hook for the PENDING state.
 */
public class PENDINGReviewPostSaveHook implements PostSaveHook<Review> {
	@Override
    public void execute(State startState, State endState, Review review, TransientMap map) {
    }
}
