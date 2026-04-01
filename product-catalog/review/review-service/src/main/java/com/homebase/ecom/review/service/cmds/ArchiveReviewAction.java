package com.homebase.ecom.review.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.review.model.Review;
import com.homebase.ecom.review.dto.ArchiveReviewPayload;

/**
 * STM action for archiving a review (PUBLISHED -> ARCHIVED).
 */
public class ArchiveReviewAction extends AbstractSTMTransitionAction<Review, ArchiveReviewPayload> {

    @Override
    public void transitionTo(Review review, ArchiveReviewPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        String reason = (payload.getArchiveReason() != null && !payload.getArchiveReason().trim().isEmpty())
                ? payload.getArchiveReason() : "Archived by admin";
        review.setModeratorNotes(reason);
        review.addActivity("archiveReview", "Review archived. Reason: " + reason);
        review.getTransientMap().previousPayload = payload;
    }
}
