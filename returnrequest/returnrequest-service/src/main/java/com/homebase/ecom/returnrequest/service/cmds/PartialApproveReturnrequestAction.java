package com.homebase.ecom.returnrequest.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.returnrequest.model.Returnrequest;
import com.homebase.ecom.returnrequest.dto.PartialApproveReturnrequestPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the partialApprove transition (UNDER_REVIEW -> PARTIALLY_APPROVED).
 * Only specific items are approved for return.
 */
public class PartialApproveReturnrequestAction extends AbstractSTMTransitionAction<Returnrequest,
        PartialApproveReturnrequestPayload> {

    private static final Logger log = LoggerFactory.getLogger(PartialApproveReturnrequestAction.class);

    @Override
    public void transitionTo(Returnrequest returnrequest,
                             PartialApproveReturnrequestPayload payload,
                             State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (payload.getAdjustedRefundAmount() != null) {
            returnrequest.totalRefundAmount = payload.getAdjustedRefundAmount();
        }

        if (returnrequest.returnType == null) {
            returnrequest.returnType = "REFUND";
        }

        returnrequest.reviewNotes = payload.getNotes();

        returnrequest.getTransientMap().previousPayload = payload;
        log.info("Return request {} partially approved. Approved items: {}, adjustedAmount={}",
                returnrequest.getId(), payload.getApprovedItemIds(), returnrequest.totalRefundAmount);
    }
}
