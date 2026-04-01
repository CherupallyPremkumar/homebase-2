package com.homebase.ecom.review.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.review.model.Review;
import com.homebase.ecom.review.dto.ReportReviewPayload;

/**
 * STM action for reporting a review (self-transition on PUBLISHED).
 * Increments the report count.
 */
public class ReportReviewAction extends AbstractSTMTransitionAction<Review, ReportReviewPayload> {

    @Override
    public void transitionTo(Review review, ReportReviewPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        review.setReportCount(review.getReportCount() + 1);

        String reason = (payload.getReportReason() != null && !payload.getReportReason().trim().isEmpty())
                ? payload.getReportReason() : "No reason provided";
        review.addActivity("reportReview", "Review reported. Reason: " + reason + ". Total reports: " + review.getReportCount());
        review.getTransientMap().previousPayload = payload;
    }
}
