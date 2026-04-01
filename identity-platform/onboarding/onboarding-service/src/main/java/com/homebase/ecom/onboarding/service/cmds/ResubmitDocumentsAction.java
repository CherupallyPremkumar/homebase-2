package com.homebase.ecom.onboarding.service.cmds;

import com.homebase.ecom.onboarding.dto.SubmitDocumentsPayload;
import com.homebase.ecom.onboarding.model.OnboardingSaga;
import com.homebase.ecom.onboarding.service.validator.OnboardingPolicyValidator;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Seller resubmits documents after they were requested.
 * Validates resubmission limits via policy validator.
 */
public class ResubmitDocumentsAction extends AbstractSTMTransitionAction<OnboardingSaga, SubmitDocumentsPayload> {

    private static final Logger log = LoggerFactory.getLogger(ResubmitDocumentsAction.class);

    @Autowired
    private OnboardingPolicyValidator policyValidator;

    @Override
    public void transitionTo(OnboardingSaga saga, SubmitDocumentsPayload payload,
                 State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm,
                 Transition transition) throws Exception {

        // Validate resubmission limit
        policyValidator.validateResubmissionLimit(saga);

        saga.setResubmissionCount(saga.getResubmissionCount() + 1);
        saga.setVerificationNotes(null);

        log.info("Documents resubmitted for onboarding {}, business: {}, resubmission #{}",
                saga.getId(), saga.getBusinessName(), saga.getResubmissionCount());
    }
}
