package com.homebase.ecom.review.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.review.model.Review;
import com.homebase.ecom.review.dto.SubmitReviewPayload;
import com.homebase.ecom.review.service.validator.ReviewPolicyValidator;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * STM action for submitting a review (SUBMITTED -> CHECK_AUTO_PUBLISH).
 * Validates review content via policy validator, then populates the review entity.
 */
public class SubmitReviewAction extends AbstractSTMTransitionAction<Review, SubmitReviewPayload> {

    @Autowired
    private ReviewPolicyValidator policyValidator;

    @Override
    public void transitionTo(Review review, SubmitReviewPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Policy validation: length, rating range, image count, profanity
        policyValidator.validateSubmission(payload);

        // Populate review from payload
        review.setProductId(payload.getProductId());
        review.setOrderId(payload.getOrderId());
        review.setRating(payload.getRating());
        review.setTitle(payload.getTitle());
        review.setBody(payload.getBody());
        if (payload.getImages() != null) {
            review.setImages(payload.getImages());
        }

        // Verified purchase check — delegates to validator
        boolean verified = policyValidator.isVerifiedPurchase(payload.getOrderId(), review.getCustomerId());
        review.setVerifiedPurchase(verified);

        review.addActivity("submitReview", "Review submitted for product " + payload.getProductId());
        review.getTransientMap().previousPayload = payload;
    }
}
