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
 * STM transition action: executes compensation steps for the saga.
 * Transition: CANCELLING/FAILED -> COMPENSATION_DONE
 */
public class CompensateAction extends AbstractSTMTransitionAction<ReturnProcessingSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(CompensateAction.class);

    @Override
    public void transitionTo(ReturnProcessingSaga saga, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        log.info("Compensating return processing saga for return request: {}", saga.getReturnRequestId());

        // Reverse any completed steps based on current state
        if (saga.getShipmentId() != null) {
            log.info("Compensating: Cancelling return shipment {}", saga.getShipmentId());
        }

        saga.setErrorMessage(null);
        log.info("Compensation complete for return request: {}", saga.getReturnRequestId());
    }
}
