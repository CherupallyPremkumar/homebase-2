package com.homebase.ecom.review.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.review.model.Review;
import com.homebase.ecom.review.dto.ResubmitReviewPayload;
import com.homebase.ecom.review.service.validator.ReviewPolicyValidator;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * STM action for resubmitting a review after edit request (EDIT_REQUESTED -> UNDER_MODERATION).
 */
public class ResubmitReviewAction extends AbstractSTMTransitionAction<Review, ResubmitReviewPayload> {

    @Autowired
    private ReviewPolicyValidator policyValidator;

    @Override
    public void transitionTo(Review review, ResubmitReviewPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Validate the resubmitted content
        policyValidator.validateResubmission(payload);

        // Update review content
        if (payload.getRating() > 0) {
            review.setRating(payload.getRating());
        }
        if (payload.getTitle() != null && !payload.getTitle().trim().isEmpty()) {
            review.setTitle(payload.getTitle());
        }
        if (payload.getBody() != null && !payload.getBody().trim().isEmpty()) {
            review.setBody(payload.getBody());
        }
        if (payload.getImages() != null) {
            review.setImages(payload.getImages());
        }

        review.setModeratorNotes(null); // Clear previous edit instructions
        review.addActivity("resubmitReview", "Review resubmitted after edit request");
        review.getTransientMap().previousPayload = payload;
    }
}
