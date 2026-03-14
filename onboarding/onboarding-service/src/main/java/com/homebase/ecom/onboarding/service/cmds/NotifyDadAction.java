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
 * Notifies the Hub Dad about a new supplier registration.
 * Incorporates the old NotifyDadCommand logic.
 */
public class NotifyDadAction extends AbstractSTMTransitionAction<OnboardingSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(NotifyDadAction.class);

    @Override
    public void transitionTo(OnboardingSaga saga, MinimalPayload payload,
                 State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm,
                 Transition transition) throws Exception {

        log.info("#### ACTION REQUIRED ####");
        log.info("New Saree Maker Registration: {} <{}>", saga.getSupplierName(), saga.getEmail());
        log.info("Please review their profile in the Hub Management Admin.");
    }
}
