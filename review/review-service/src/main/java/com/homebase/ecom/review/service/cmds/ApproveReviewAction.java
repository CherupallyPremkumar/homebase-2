package com.homebase.ecom.review.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.review.model.Review;
import com.homebase.ecom.review.dto.ApproveReviewPayload;

/**
 * Contains customized logic for the approveReview transition.
 */
public class ApproveReviewAction extends AbstractSTMTransitionAction<Review, ApproveReviewPayload> {

	@Override
	public void transitionTo(Review review,
            ApproveReviewPayload payload,
            State startState, String eventId,
			State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
            // Mark the review as approved and track the approval
            review.addActivity("approveReview", "Review approved by admin moderator");

            // If the review is for a verified purchase, ensure badge is set
            // (verifiedPurchase is set at creation time based on order lookup)

            review.getTransientMap().previousPayload = payload;
	}

}
