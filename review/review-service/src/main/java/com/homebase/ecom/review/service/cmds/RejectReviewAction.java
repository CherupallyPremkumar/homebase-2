package com.homebase.ecom.review.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.review.model.Review;
import com.homebase.ecom.review.dto.RejectReviewPayload;

/**
 * STM action for rejecting a review (UNDER_MODERATION/FLAGGED -> REJECTED).
 * Requires a rejection reason.
 */
public class RejectReviewAction extends AbstractSTMTransitionAction<Review, RejectReviewPayload> {

    @Override
    public void transitionTo(Review review, RejectReviewPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (payload.getRejectionReason() == null || payload.getRejectionReason().trim().isEmpty()) {
            throw new IllegalArgumentException("Rejection reason is required when rejecting a review");
        }

        review.setModeratorNotes(payload.getRejectionReason());
        review.addActivity("rejectReview", "Review rejected. Reason: " + payload.getRejectionReason());
        review.getTransientMap().previousPayload = payload;
    }
}
