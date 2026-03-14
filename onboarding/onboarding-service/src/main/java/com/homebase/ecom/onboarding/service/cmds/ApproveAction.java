package com.homebase.ecom.onboarding.service.cmds;

import com.homebase.ecom.onboarding.dto.ApproveOnboardingPayload;
import com.homebase.ecom.onboarding.model.OnboardingSaga;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Admin approves the onboarding application.
 * Sets default commission if not already specified.
 */
public class ApproveAction extends AbstractSTMTransitionAction<OnboardingSaga, ApproveOnboardingPayload> {

    private static final Logger log = LoggerFactory.getLogger(ApproveAction.class);
    private static final double DEFAULT_COMMISSION = 10.0;

    @Override
    public void transitionTo(OnboardingSaga saga, ApproveOnboardingPayload payload,
                 State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm,
                 Transition transition) throws Exception {

        // Use commission override from approval payload if provided
        if (payload.getCommissionOverride() != null) {
            saga.setCommissionPercentage(payload.getCommissionOverride());
        } else if (saga.getCommissionPercentage() == null) {
            saga.setCommissionPercentage(DEFAULT_COMMISSION);
        }

        // Clear any previous error
        saga.setErrorMessage(null);

        log.info("Onboarding application APPROVED for supplier: {}, commission: {}%",
                saga.getSupplierName(), saga.getCommissionPercentage());
    }
}
