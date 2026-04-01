package com.homebase.ecom.onboarding.service.cmds;

import com.homebase.ecom.onboarding.dto.VerifyBusinessPayload;
import com.homebase.ecom.onboarding.model.OnboardingSaga;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Admin/compliance verifies the business details and credentials.
 * Transitions from DOCUMENT_VERIFICATION to BUSINESS_VERIFICATION.
 */
public class VerifyBusinessAction extends AbstractSTMTransitionAction<OnboardingSaga, VerifyBusinessPayload> {

    private static final Logger log = LoggerFactory.getLogger(VerifyBusinessAction.class);

    @Override
    public void transitionTo(OnboardingSaga saga, VerifyBusinessPayload payload,
                 State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm,
                 Transition transition) throws Exception {

        if (saga.getBusinessName() == null || saga.getBusinessName().isBlank()) {
            throw new IllegalArgumentException("Business name is required for business verification");
        }

        String notes = saga.getVerificationNotes();
        if (payload.getVerificationNotes() != null) {
            notes = (notes != null ? notes + "\n" : "") + "Business: " + payload.getVerificationNotes();
        }
        saga.setVerificationNotes(notes);

        log.info("Business verified for onboarding {}, business: {}, type: {}",
                saga.getId(), saga.getBusinessName(), saga.getBusinessType());
    }
}
