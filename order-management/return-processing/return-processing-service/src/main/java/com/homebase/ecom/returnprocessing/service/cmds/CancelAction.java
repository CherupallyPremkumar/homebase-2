package com.homebase.ecom.returnprocessing.service.cmds;

import com.homebase.ecom.returnprocessing.model.ReturnProcessingSaga;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM transition action: cancels the saga and initiates compensation.
 * Transition: INITIATED/PICKUP_SCHEDULED -> CANCELLING
 */
public class CancelAction extends AbstractSTMTransitionAction<ReturnProcessingSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(CancelAction.class);

    @Override
    public void transitionTo(ReturnProcessingSaga saga, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        log.info("Cancelling return processing saga for return request: {}, from state: {}",
                saga.getReturnRequestId(), startState.getStateId());

        if (payload != null && payload.getComment() != null) {
            saga.setErrorMessage("Cancelled: " + payload.getComment());
        }
    }
}
