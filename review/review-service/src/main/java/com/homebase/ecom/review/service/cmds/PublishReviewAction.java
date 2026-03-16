package com.homebase.ecom.review.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.review.model.Review;
import com.homebase.ecom.review.dto.PublishReviewPayload;

/**
 * STM action for publishing a review (UNDER_MODERATION/FLAGGED -> PUBLISHED).
 */
public class PublishReviewAction extends AbstractSTMTransitionAction<Review, PublishReviewPayload> {

    @Override
    public void transitionTo(Review review, PublishReviewPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (payload.getModeratorNotes() != null && !payload.getModeratorNotes().trim().isEmpty()) {
            review.setModeratorNotes(payload.getModeratorNotes());
        }

        review.addActivity("publishReview", "Review published by moderator from " + startState.getStateId());
        review.getTransientMap().previousPayload = payload;
    }
}
