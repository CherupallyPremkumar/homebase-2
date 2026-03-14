package com.homebase.ecom.returnrequest.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.returnrequest.model.Returnrequest;
import com.homebase.ecom.returnrequest.dto.RejectReturnReturnrequestPayload;
import com.homebase.ecom.returnrequest.service.validator.ReturnRequestPolicyValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Handles the rejectReturn transition (UNDER_REVIEW -> REJECTED).
 * Enforces comment-required-on-reject policy.
 * Records rejection reason and comment.
 */
public class RejectReturnReturnrequestAction extends AbstractSTMTransitionAction<Returnrequest,
        RejectReturnReturnrequestPayload> {

    private static final Logger log = LoggerFactory.getLogger(RejectReturnReturnrequestAction.class);

    @Autowired
    private ReturnRequestPolicyValidator policyValidator;

    @Override
    public void transitionTo(Returnrequest returnrequest,
                             RejectReturnReturnrequestPayload payload,
                             State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Enforce comment-required-on-reject policy
        if (policyValidator.isCommentRequiredOnReject()) {
            String comment = payload.getComment();
            if (comment == null || comment.trim().isEmpty()) {
                throw new IllegalArgumentException("Comment required when rejecting a return request.");
            }
        }

        // Record rejection details
        if (payload.getRejectionReason() != null) {
            returnrequest.rejectionReason = payload.getRejectionReason();
        }
        returnrequest.rejectionComment = payload.getComment();

        returnrequest.getTransientMap().previousPayload = payload;
        log.info("Return request {} rejected. Reason: {}", returnrequest.getId(), returnrequest.rejectionReason);
    }
}
