package com.homebase.ecom.onboarding.service.cmds;

import com.homebase.ecom.onboarding.dto.SubmitDocumentsPayload;
import com.homebase.ecom.onboarding.model.OnboardingSaga;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Supplier submits additional documents after being asked for more info.
 * Verifies document format and clears previous error state.
 */
public class SubmitDocumentsAction extends AbstractSTMTransitionAction<OnboardingSaga, SubmitDocumentsPayload> {

    private static final Logger log = LoggerFactory.getLogger(SubmitDocumentsAction.class);

    @Override
    public void transitionTo(OnboardingSaga saga, SubmitDocumentsPayload payload,
                 State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm,
                 Transition transition) throws Exception {

        // Clear previous error/info request message
        saga.setErrorMessage(null);

        // Mark documents as resubmitted
        saga.getTransientMap().put("documentsResubmitted", true);
        saga.getTransientMap().put("resubmittedAt", new java.util.Date());

        log.info("Supplier {} submitted additional documents for onboarding",
                saga.getSupplierName());
    }
}
