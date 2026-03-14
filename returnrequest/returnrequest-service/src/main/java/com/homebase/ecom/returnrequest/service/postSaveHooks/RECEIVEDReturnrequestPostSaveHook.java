package com.homebase.ecom.returnrequest.service.postSaveHooks;

import com.homebase.ecom.returnrequest.model.Returnrequest;
import com.homebase.ecom.returnrequest.service.event.ReturnReceivedEvent;
import com.homebase.ecom.returnrequest.service.event.ReturnRequestEventPublisher;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * PostSaveHook for RECEIVED state.
 * Publishes ReturnReceivedEvent after the returned item is received at warehouse.
 */
public class RECEIVEDReturnrequestPostSaveHook implements PostSaveHook<Returnrequest> {

    private static final Logger log = LoggerFactory.getLogger(RECEIVEDReturnrequestPostSaveHook.class);

    @Autowired
    private ReturnRequestEventPublisher eventPublisher;

    @Override
    public void execute(State startState, State endState, Returnrequest returnrequest, TransientMap map) {
        ReturnReceivedEvent event = new ReturnReceivedEvent(
                returnrequest.getId(),
                returnrequest.orderId,
                returnrequest.warehouseId,
                returnrequest.conditionOnReceipt
        );
        log.info("Return request {} received at warehouse, publishing ReturnReceivedEvent", returnrequest.getId());
        eventPublisher.publishReturnReceived(event);
    }
}
