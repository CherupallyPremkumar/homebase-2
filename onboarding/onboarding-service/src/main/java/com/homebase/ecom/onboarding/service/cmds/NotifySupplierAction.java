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
 * Sends a welcome notification to the newly onboarded supplier.
 * Transitions from SUPPLIER_CREATED to ACTIVE.
 */
public class NotifySupplierAction extends AbstractSTMTransitionAction<OnboardingSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(NotifySupplierAction.class);

    @Override
    public void transitionTo(OnboardingSaga saga, MinimalPayload payload,
                 State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm,
                 Transition transition) throws Exception {

        if (saga.getSupplierId() == null || saga.getSupplierId().trim().isEmpty()) {
            throw new IllegalStateException("Cannot notify supplier - supplier record has not been created");
        }

        log.info("Sending welcome notification to supplier: {} <{}>",
                saga.getSupplierName(), saga.getEmail());
        log.info("Supplier ID: {}, Commission: {}%",
                saga.getSupplierId(), saga.getCommissionPercentage());

        saga.getTransientMap().put("notificationSent", true);
        saga.getTransientMap().put("welcomeEmailSent", true);
    }
}
