package com.homebase.ecom.review.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.review.model.Review;
import com.homebase.ecom.review.dto.MarkHelpfulPayload;

/**
 * STM action for marking a review as helpful (self-transition on PUBLISHED).
 */
public class MarkHelpfulAction extends AbstractSTMTransitionAction<Review, MarkHelpfulPayload> {

    @Override
    public void transitionTo(Review review, MarkHelpfulPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        review.setHelpfulCount(review.getHelpfulCount() + 1);
        review.addActivity("markHelpful", "Review marked as helpful. Total: " + review.getHelpfulCount());
        review.getTransientMap().previousPayload = payload;
    }
}
