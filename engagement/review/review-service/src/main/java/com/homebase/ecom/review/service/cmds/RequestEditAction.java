package com.homebase.ecom.review.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.review.model.Review;
import com.homebase.ecom.review.dto.RequestEditPayload;

/**
 * STM action for requesting edits from the reviewer (UNDER_MODERATION -> EDIT_REQUESTED).
 */
public class RequestEditAction extends AbstractSTMTransitionAction<Review, RequestEditPayload> {

    @Override
    public void transitionTo(Review review, RequestEditPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (payload.getEditInstructions() == null || payload.getEditInstructions().trim().isEmpty()) {
            throw new IllegalArgumentException("Edit instructions are required when requesting edits");
        }

        review.setModeratorNotes(payload.getEditInstructions());
        review.addActivity("requestEdit", "Edit requested: " + payload.getEditInstructions());
        review.getTransientMap().previousPayload = payload;
    }
}
