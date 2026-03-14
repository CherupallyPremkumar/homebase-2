package com.homebase.ecom.returnrequest.service.postSaveHooks;

import com.homebase.ecom.returnrequest.model.Returnrequest;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PostSaveHook for IN_TRANSIT_BACK state.
 * Logs that the return pickup has been initiated and item is in transit.
 */
public class IN_TRANSIT_BACKReturnrequestPostSaveHook implements PostSaveHook<Returnrequest> {

    private static final Logger log = LoggerFactory.getLogger(IN_TRANSIT_BACKReturnrequestPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Returnrequest returnrequest, TransientMap map) {
        log.info("Return request {} is now in transit back to warehouse, tracking: {}",
                returnrequest.getId(), returnrequest.pickupTrackingNumber);
    }
}
