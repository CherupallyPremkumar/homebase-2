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
 * Sets refund amount and return type for downstream processing.
 */
public class ApproveReturnReturnrequestAction extends AbstractSTMTransitionAction<Returnrequest,
        ApproveReturnReturnrequestPayload> {

    private static final Logger log = LoggerFactory.getLogger(ApproveReturnReturnrequestAction.class);

    @Override
    public void transitionTo(Returnrequest returnrequest,
                             ApproveReturnReturnrequestPayload payload,
                             State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Set refund amount from payload or keep existing
        if (payload.getRefundAmount() != null) {
            returnrequest.totalRefundAmount = payload.getRefundAmount();
        }

        // Set return type
        if (payload.getRefundType() != null) {
            returnrequest.returnType = payload.getRefundType();
        } else if (returnrequest.returnType == null) {
            returnrequest.returnType = "REFUND";
        }

        // Calculate restocking fee (0% default)
        if (returnrequest.restockingFee == null) {
            returnrequest.restockingFee = BigDecimal.ZERO;
        }

        returnrequest.getTransientMap().previousPayload = payload;
        log.info("Return request {} approved with returnType={}, totalRefundAmount={}",
                returnrequest.getId(), returnrequest.returnType, returnrequest.totalRefundAmount);
    }
}
