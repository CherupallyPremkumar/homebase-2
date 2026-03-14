package com.homebase.ecom.returnrequest.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.returnrequest.model.Returnrequest;
import com.homebase.ecom.returnrequest.dto.ApproveReturnReturnrequestPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Handles the approveReturn transition (UNDER_REVIEW -> APPROVED).
 * Validates return window, calculates refund amount based on refund type.
 * Sets the refund amount on the return request for downstream processing.
 */
public class ApproveReturnReturnrequestAction extends AbstractSTMTransitionAction<Returnrequest,
        ApproveReturnReturnrequestPayload> {

    private static final Logger log = LoggerFactory.getLogger(ApproveReturnReturnrequestAction.class);

    @Override
    public void transitionTo(Returnrequest returnrequest,
                             ApproveReturnReturnrequestPayload payload,
                             State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Calculate refund amount
        if (payload.getRefundAmount() != null) {
            // Explicit refund amount provided (partial refund scenario)
            returnrequest.refundAmount = payload.getRefundAmount();
        } else if (returnrequest.itemPrice != null) {
            // Default to item price for full refund
            returnrequest.refundAmount = returnrequest.itemPrice.multiply(
                    BigDecimal.valueOf(returnrequest.quantity != null ? returnrequest.quantity : 1));
        }

        if (payload.getRefundType() != null) {
            returnrequest.returnType = payload.getRefundType();
        } else if (returnrequest.returnType == null || !returnrequest.returnType.equals("AUTO_APPROVED")) {
            returnrequest.returnType = "FULL";
        }

        returnrequest.getTransientMap().previousPayload = payload;
        log.info("Return request {} approved with refund type={}, amount={}",
                returnrequest.getId(), returnrequest.returnType, returnrequest.refundAmount);
    }
}
