package com.homebase.ecom.review.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.review.model.Review;
import com.homebase.ecom.review.dto.RejectReviewPayload;

/**
 * Contains customized logic for the rejectReview transition.
 */
public class RejectReviewAction extends AbstractSTMTransitionAction<Review, RejectReviewPayload> {

	@Override
	public void transitionTo(Review review,
            RejectReviewPayload payload,
            State startState, String eventId,
			State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
            // Require a rejection reason
            if (payload.getRejectionReason() == null || payload.getRejectionReason().trim().isEmpty()) {
                throw new IllegalArgumentException("Rejection reason is required when rejecting a review");
            }
            review.description = payload.getRejectionReason();
            review.addActivity("rejectReview", "Review rejected. Reason: " + payload.getRejectionReason());

            // The reviewer notification is handled in the REJECTEDReviewPostSaveHook
            review.getTransientMap().previousPayload = payload;
	}

}
