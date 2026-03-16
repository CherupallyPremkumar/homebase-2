package com.homebase.ecom.review.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.review.model.Review;
import com.homebase.ecom.review.dto.ModerateReviewPayload;

/**
 * STM action for sending a flagged review back to moderation (FLAGGED -> UNDER_MODERATION).
 */
public class ModerateReviewAction extends AbstractSTMTransitionAction<Review, ModerateReviewPayload> {

    @Override
    public void transitionTo(Review review, ModerateReviewPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (payload.getModeratorNotes() != null && !payload.getModeratorNotes().trim().isEmpty()) {
            review.setModeratorNotes(payload.getModeratorNotes());
        }

        review.addActivity("moderateReview", "Review sent to moderation from " + startState.getStateId());
        review.getTransientMap().previousPayload = payload;
    }
}
