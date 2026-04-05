package com.homebase.ecom.returnrequest.service.postSaveHooks;

import com.homebase.ecom.returnrequest.model.Returnrequest;
import com.homebase.ecom.returnrequest.service.event.ReturnRequestEventPublisher;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * PostSaveHook for REQUESTED state.
 * Publishes RETURN_REQUESTED event.
 */
public class REQUESTEDReturnrequestPostSaveHook implements PostSaveHook<Returnrequest> {

    private static final Logger log = LoggerFactory.getLogger(REQUESTEDReturnrequestPostSaveHook.class);

    @Autowired
    private ReturnRequestEventPublisher eventPublisher;

    @Override
    public void execute(State startState, State endState, Returnrequest returnrequest, TransientMap map) {
        log.info("Return request {} created for order {}, customer {}, reason: {}",
                returnrequest.getId(), returnrequest.orderId,
                returnrequest.customerId, returnrequest.reason);
        eventPublisher.publishReturnRequested(returnrequest);
    }
}
