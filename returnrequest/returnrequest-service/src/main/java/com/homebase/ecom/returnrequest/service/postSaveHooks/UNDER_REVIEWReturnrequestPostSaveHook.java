package com.homebase.ecom.returnrequest.service.postSaveHooks;

import com.homebase.ecom.returnrequest.model.Returnrequest;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PostSaveHook for UNDER_REVIEW state.
 * Logs that the return request is now under quality review.
 */
public class UNDER_REVIEWReturnrequestPostSaveHook implements PostSaveHook<Returnrequest> {

    private static final Logger log = LoggerFactory.getLogger(UNDER_REVIEWReturnrequestPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Returnrequest returnrequest, TransientMap map) {
        log.info("Return request {} is now under review by inspector {}",
                returnrequest.getId(), returnrequest.inspectorId);
    }
}
