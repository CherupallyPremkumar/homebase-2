package com.homebase.ecom.onboarding.service.cmds;

import com.homebase.ecom.onboarding.dto.CompleteTrainingPayload;
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
 * Seller completes a training module.
 * Transitions from TRAINING to CHECK_TRAINING_COMPLETE (auto-state).
 */
public class CompleteTrainingAction extends AbstractSTMTransitionAction<OnboardingSaga, CompleteTrainingPayload> {

    private static final Logger log = LoggerFactory.getLogger(CompleteTrainingAction.class);

    @Autowired
    private OnboardingPolicyValidator policyValidator;

    @Override
    public void transitionTo(OnboardingSaga saga, CompleteTrainingPayload payload,
                 State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm,
                 Transition transition) throws Exception {

        String moduleId = payload.getModuleId();
        if (moduleId == null || moduleId.isBlank()) {
            throw new IllegalArgumentException("Module ID is required");
        }

        if (saga.getTrainingCompletedModules().contains(moduleId)) {
            log.warn("Module {} already completed for onboarding {}", moduleId, saga.getId());
            return;
        }

        saga.getTrainingCompletedModules().add(moduleId);
        int required = policyValidator.getTrainingModulesRequired();
        int completed = saga.getCompletedModuleCount();
        saga.setTrainingProgress(Math.min(100, (completed * 100) / required));

        log.info("Training module {} completed for onboarding {}, progress: {}/{} ({}%)",
                moduleId, saga.getId(), completed, required, saga.getTrainingProgress());
    }
}
