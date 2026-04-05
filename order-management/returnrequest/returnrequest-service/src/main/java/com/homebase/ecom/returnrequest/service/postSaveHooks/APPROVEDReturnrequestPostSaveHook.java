package com.homebase.ecom.returnrequest.service.postSaveHooks;

import com.homebase.ecom.returnrequest.domain.port.NotificationPort;
import com.homebase.ecom.returnrequest.model.Returnrequest;
import com.homebase.ecom.returnrequest.service.event.ReturnRequestEventPublisher;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * PostSaveHook for APPROVED state.
 * Notifies customer + creates shipping label. Publishes RETURN_APPROVED event.
 */
public class APPROVEDReturnrequestPostSaveHook implements PostSaveHook<Returnrequest> {

    private static final Logger log = LoggerFactory.getLogger(APPROVEDReturnrequestPostSaveHook.class);

    @Autowired
    private ReturnRequestEventPublisher eventPublisher;

    @Autowired
    private NotificationPort notificationPort;

    @Override
    public void execute(State startState, State endState, Returnrequest returnrequest, TransientMap map) {
        log.info("Return request {} approved, publishing RETURN_APPROVED and notifying customer", returnrequest.getId());

        // Notify customer
        if (notificationPort != null) {
            notificationPort.notifyReturnApproved(
                    returnrequest.getId(), returnrequest.customerId, returnrequest.orderId);
        }

        // Publish event
        eventPublisher.publishReturnApproved(returnrequest);
    }
}
