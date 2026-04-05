package com.homebase.ecom.review.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.review.model.Review;
import com.homebase.ecom.review.dto.FlagReviewPayload;

/**
 * STM action for flagging a review (PUBLISHED -> FLAGGED).
 * Can be triggered by CUSTOMER or SYSTEM.
 */
public class FlagReviewAction extends AbstractSTMTransitionAction<Review, FlagReviewPayload> {

    @Override
    public void transitionTo(Review review, FlagReviewPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        String reason = (payload.getFlagReason() != null && !payload.getFlagReason().trim().isEmpty())
                ? payload.getFlagReason() : "Flagged for re-moderation";
        review.setModeratorNotes(reason);
        review.addActivity("flagReview", "Review flagged from " + startState.getStateId() + ". Reason: " + reason);
        review.getTransientMap().previousPayload = payload;
    }
}
