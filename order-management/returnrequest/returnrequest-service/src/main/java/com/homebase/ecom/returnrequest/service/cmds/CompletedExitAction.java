package com.homebase.ecom.returnrequest.service.cmds;

import com.homebase.ecom.returnrequest.model.Returnrequest;
import org.chenile.stm.State;
import org.chenile.stm.action.STMAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exit action triggered when a return request reaches REFUND_INITIATED state.
 * Marks the settlement adjustment requirement.
 */
public class CompletedExitAction implements STMAction<Returnrequest> {

    private static final Logger log = LoggerFactory.getLogger(CompletedExitAction.class);

    @Override
    public void execute(State fromState, State toState, Returnrequest returnrequest) throws Exception {
        log.info("Return request {} refund initiated, settlement adjustment required", returnrequest.getId());
        returnrequest.getTransientMap().put("settlementAdjustmentRequired", true);
    }
}
