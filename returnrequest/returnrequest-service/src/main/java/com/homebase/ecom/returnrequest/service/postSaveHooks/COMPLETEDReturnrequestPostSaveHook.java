package com.homebase.ecom.returnrequest.service.postSaveHooks;

import com.homebase.ecom.returnrequest.domain.port.InventoryPort;
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
 * PostSaveHook for COMPLETED state.
 * Updates inventory (restock) and publishes RETURN_COMPLETED event.
 */
public class COMPLETEDReturnrequestPostSaveHook implements PostSaveHook<Returnrequest> {

    private static final Logger log = LoggerFactory.getLogger(COMPLETEDReturnrequestPostSaveHook.class);

    @Autowired(required = false)
    private InventoryPort inventoryPort;

    @Autowired(required = false)
    private NotificationPort notificationPort;

    @Autowired
    private ReturnRequestEventPublisher eventPublisher;

    @Override
    public void execute(State startState, State endState, Returnrequest returnrequest, TransientMap map) {
        log.info("Return request {} completed, updating inventory and notifying customer", returnrequest.getId());

        // Update inventory - restock returned items
        if (inventoryPort != null && returnrequest.items != null && !returnrequest.items.isEmpty()) {
            inventoryPort.restockReturnedItems(
                    returnrequest.getId(), returnrequest.items, returnrequest.warehouseId);
        }

        // Notify customer
        if (notificationPort != null) {
            notificationPort.notifyReturnCompleted(
                    returnrequest.getId(), returnrequest.customerId, returnrequest.orderId);
        }

        // Publish event
        eventPublisher.publishReturnCompleted(returnrequest);
    }
}
