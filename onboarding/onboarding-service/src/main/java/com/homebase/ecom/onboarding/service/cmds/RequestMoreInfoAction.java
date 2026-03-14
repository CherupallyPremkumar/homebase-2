package com.homebase.ecom.onboarding.service.cmds;

import com.homebase.ecom.onboarding.model.OnboardingSaga;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Admin requests additional documents/information from the supplier.
 * Specifies what documents or info is missing.
 */
public class RequestMoreInfoAction extends AbstractSTMTransitionAction<OnboardingSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(RequestMoreInfoAction.class);

    @Override
    public void transitionTo(OnboardingSaga saga, MinimalPayload payload,
                 State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm,
                 Transition transition) throws Exception {

        String comment = payload.getComment();
        if (comment == null || comment.trim().isEmpty()) {
            throw new IllegalArgumentException("Must specify what additional information is needed");
        }

        // Store the requested info details
        saga.setErrorMessage("Additional info requested: " + comment);

        // Flag for notification to supplier
        saga.getTransientMap().put("moreInfoNotificationRequired", true);

        log.info("Requesting more info for onboarding application: {}, details: {}",
                saga.getSupplierName(), comment);
    }
}
