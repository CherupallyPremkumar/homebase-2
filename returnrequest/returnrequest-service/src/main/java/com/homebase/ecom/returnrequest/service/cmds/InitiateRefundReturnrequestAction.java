package com.homebase.ecom.returnrequest.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.returnrequest.model.Returnrequest;
import com.homebase.ecom.returnrequest.dto.InitiateRefundReturnrequestPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Handles the initiateRefund transition (INSPECTED -> REFUND_INITIATED).
 * System or Finance triggers the refund after item inspection.
 */
public class InitiateRefundReturnrequestAction extends AbstractSTMTransitionAction<Returnrequest,
        InitiateRefundReturnrequestPayload> {

    private static final Logger log = LoggerFactory.getLogger(InitiateRefundReturnrequestAction.class);

    @Override
    public void transitionTo(Returnrequest returnrequest,
                             InitiateRefundReturnrequestPayload payload,
                             State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Override refund amount if provided
        if (payload.getFinalRefundAmount() != null) {
            returnrequest.totalRefundAmount = payload.getFinalRefundAmount();
        }

        // Apply restocking fee
        if (returnrequest.restockingFee != null && returnrequest.restockingFee.compareTo(BigDecimal.ZERO) > 0
                && returnrequest.totalRefundAmount != null) {
            BigDecimal feeAmount = returnrequest.totalRefundAmount
                    .multiply(returnrequest.restockingFee)
                    .divide(BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP);
            returnrequest.totalRefundAmount = returnrequest.totalRefundAmount.subtract(feeAmount);
        }

        returnrequest.getTransientMap().previousPayload = payload;
        log.info("Return request {} refund initiated: amount={}, returnType={}",
                returnrequest.getId(), returnrequest.totalRefundAmount, returnrequest.returnType);
    }
}
