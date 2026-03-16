package com.homebase.ecom.returnrequest.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.returnrequest.model.Returnrequest;
import com.homebase.ecom.returnrequest.dto.ReviewReturnReturnrequestPayload;
import com.homebase.ecom.returnrequest.service.validator.ReturnRequestPolicyValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Handles the reviewReturn transition (REQUESTED -> CHECK_AUTO_APPROVE).
 * Validates return window, item count, value limits via policy validator.
 * Calculates totalRefundAmount for auto-approve check.
 */
public class ReviewReturnReturnrequestAction extends AbstractSTMTransitionAction<Returnrequest,
        ReviewReturnReturnrequestPayload> {

    private static final Logger log = LoggerFactory.getLogger(ReviewReturnReturnrequestAction.class);

    @Autowired
    private ReturnRequestPolicyValidator policyValidator;

    @Override
    public void transitionTo(Returnrequest returnrequest,
                             ReviewReturnReturnrequestPayload payload,
                             State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Validate return window
        policyValidator.validateReturnWindow(returnrequest.orderDeliveryDate);

        // Validate item count
        policyValidator.validateItemCount(returnrequest.items != null ? returnrequest.items.size() : 0);

        // Validate return reason
        policyValidator.validateReturnReason(returnrequest.reason);

        // Record reviewer details
        if (payload.getReviewerId() != null) {
            returnrequest.reviewerId = payload.getReviewerId();
        }
        if (payload.getReviewNotes() != null) {
            returnrequest.reviewNotes = payload.getReviewNotes();
        }

        // Ensure totalRefundAmount is calculated for auto-approve check
        if (returnrequest.totalRefundAmount == null) {
            returnrequest.totalRefundAmount = BigDecimal.ZERO;
        }

        returnrequest.getTransientMap().previousPayload = payload;
        log.info("Return request {} reviewed by {}, totalRefundAmount={}, moving to CHECK_AUTO_APPROVE",
                returnrequest.getId(), returnrequest.reviewerId, returnrequest.totalRefundAmount);
    }
}
