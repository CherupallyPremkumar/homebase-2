package com.homebase.ecom.returnrequest.service.postSaveHooks;

import com.homebase.ecom.returnrequest.model.Returnrequest;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PostSaveHook for INSPECTED state.
 * Logs inspection completion.
 */
public class INSPECTEDReturnrequestPostSaveHook implements PostSaveHook<Returnrequest> {

    private static final Logger log = LoggerFactory.getLogger(INSPECTEDReturnrequestPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Returnrequest returnrequest, TransientMap map) {
        log.info("Return request {} inspected by {}, ready for refund processing",
                returnrequest.getId(), returnrequest.inspectorId);
    }
}
