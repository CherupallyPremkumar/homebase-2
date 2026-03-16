package com.homebase.ecom.onboarding.service.cmds;

import com.homebase.ecom.onboarding.dto.VerifyDocumentsPayload;
import com.homebase.ecom.onboarding.model.OnboardingSaga;
import com.homebase.ecom.onboarding.model.OnboardingDocument;
import com.homebase.ecom.onboarding.service.validator.OnboardingPolicyValidator;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Admin/compliance verifies the submitted documents.
 * Validates document completeness via policy validator.
 */
public class VerifyDocumentsAction extends AbstractSTMTransitionAction<OnboardingSaga, VerifyDocumentsPayload> {

    private static final Logger log = LoggerFactory.getLogger(VerifyDocumentsAction.class);

    @Autowired
    private OnboardingPolicyValidator policyValidator;

    @Override
    public void transitionTo(OnboardingSaga saga, VerifyDocumentsPayload payload,
                 State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm,
                 Transition transition) throws Exception {

        // Validate document completeness
        policyValidator.validateDocumentCompleteness(saga);

        // Mark all documents as verified
        if (saga.getDocuments() != null) {
            for (OnboardingDocument doc : saga.getDocuments()) {
                if (doc.isPending()) {
                    doc.setStatus("VERIFIED");
                    doc.setVerifiedAt(LocalDateTime.now());
                }
            }
        }

        saga.setVerificationNotes(payload.getVerificationNotes());
        log.info("Documents verified for onboarding application {}, business: {}",
                saga.getId(), saga.getBusinessName());
    }
}
