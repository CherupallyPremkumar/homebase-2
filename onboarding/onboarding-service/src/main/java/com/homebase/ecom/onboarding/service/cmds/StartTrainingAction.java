package com.homebase.ecom.onboarding.service.cmds;

import com.homebase.ecom.onboarding.model.OnboardingSaga;
import com.homebase.ecom.onboarding.port.TrainingPort;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * System triggers training phase for the supplier.
 * Transitions from BUSINESS_VERIFICATION to TRAINING.
 */
public class StartTrainingAction extends AbstractSTMTransitionAction<OnboardingSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(StartTrainingAction.class);

    @Autowired(required = false)
    private TrainingPort trainingPort;

    @Override
    public void transitionTo(OnboardingSaga saga, MinimalPayload payload,
                 State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm,
                 Transition transition) throws Exception {

        saga.setTrainingProgress(0);
        saga.getTrainingCompletedModules().clear();

        if (trainingPort != null) {
            trainingPort.initializeTraining(saga.getId(), saga.getSupplierId());
        }

        log.info("Training started for onboarding {}, business: {}",
                saga.getId(), saga.getBusinessName());
    }
}
