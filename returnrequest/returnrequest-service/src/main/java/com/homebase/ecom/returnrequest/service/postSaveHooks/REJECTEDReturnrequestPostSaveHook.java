package com.homebase.ecom.returnrequest.service.postSaveHooks;

import com.homebase.ecom.returnrequest.model.Returnrequest;
import com.homebase.ecom.returnrequest.service.event.ReturnRejectedEvent;
import com.homebase.ecom.returnrequest.service.event.ReturnRequestEventPublisher;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * PostSaveHook for REJECTED state.
 * Publishes ReturnRejectedEvent after the return request is rejected.
 */
public class REJECTEDReturnrequestPostSaveHook implements PostSaveHook<Returnrequest> {

    private static final Logger log = LoggerFactory.getLogger(REJECTEDReturnrequestPostSaveHook.class);

    @Autowired
    private ReturnRequestEventPublisher eventPublisher;

    @Override
    public void execute(State startState, State endState, Returnrequest returnrequest, TransientMap map) {
        ReturnRejectedEvent event = new ReturnRejectedEvent(
                returnrequest.getId(),
                returnrequest.orderId,
                returnrequest.rejectionReason,
                returnrequest.rejectionComment
        );
        log.info("Return request {} rejected, publishing ReturnRejectedEvent", returnrequest.getId());
        eventPublisher.publishReturnRejected(event);
    }
}
