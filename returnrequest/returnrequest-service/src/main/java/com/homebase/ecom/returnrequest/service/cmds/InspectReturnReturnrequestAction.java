package com.homebase.ecom.returnrequest.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.returnrequest.model.Returnrequest;
import com.homebase.ecom.returnrequest.dto.InspectReturnReturnrequestPayload;
import com.homebase.ecom.returnrequest.service.validator.ReturnRequestPolicyValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Handles the inspectReturn transition (REQUESTED -> UNDER_REVIEW).
 * Quality checker inspects return reason and product condition.
 * Validates the return window and return reason via policy validator.
 * If the item qualifies for auto-approval (low value), it can be auto-approved.
 */
public class InspectReturnReturnrequestAction extends AbstractSTMTransitionAction<Returnrequest,
        InspectReturnReturnrequestPayload> {

    private static final Logger log = LoggerFactory.getLogger(InspectReturnReturnrequestAction.class);

    @Autowired
    private ReturnRequestPolicyValidator policyValidator;

    @Override
    public void transitionTo(Returnrequest returnrequest,
                             InspectReturnReturnrequestPayload payload,
                             State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Validate return window
        if (returnrequest.orderDeliveryDate != null) {
            long daysSinceDelivery = ChronoUnit.DAYS.between(returnrequest.orderDeliveryDate, LocalDateTime.now());
            int maxWindow = policyValidator.getMaxReturnWindowDays();
            if (daysSinceDelivery > maxWindow) {
                throw new IllegalArgumentException("Return window expired. Maximum " + maxWindow
                        + " days allowed, but " + daysSinceDelivery + " days have passed since delivery.");
            }
        }

        // Validate return reason
        policyValidator.validateReturnReason(returnrequest.reason);

        // Record inspector details
        if (payload.getInspectorId() != null) {
            returnrequest.inspectorId = payload.getInspectorId();
        }
        if (payload.getInspectorNotes() != null) {
            returnrequest.inspectorNotes = payload.getInspectorNotes();
        }

        // Check if auto-approve is possible for low-value items
        if (returnrequest.itemPrice != null && policyValidator.canAutoApprove(returnrequest.itemPrice)) {
            returnrequest.returnType = "AUTO_APPROVED";
            log.info("Return request {} qualifies for auto-approval (itemPrice={} below threshold)",
                    returnrequest.getId(), returnrequest.itemPrice);
        }

        returnrequest.getTransientMap().previousPayload = payload;
        log.info("Return request {} inspected by {}, moving to UNDER_REVIEW",
                returnrequest.getId(), returnrequest.inspectorId);
    }
}
