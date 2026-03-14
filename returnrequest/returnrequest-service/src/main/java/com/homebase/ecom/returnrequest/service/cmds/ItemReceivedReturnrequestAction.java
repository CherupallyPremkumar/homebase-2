package com.homebase.ecom.returnrequest.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.returnrequest.model.Returnrequest;
import com.homebase.ecom.returnrequest.dto.ItemReceivedReturnrequestPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the itemReceived transition (IN_TRANSIT_BACK -> RECEIVED).
 * Warehouse confirms receipt and inspects the returned item condition.
 */
public class ItemReceivedReturnrequestAction extends AbstractSTMTransitionAction<Returnrequest,
        ItemReceivedReturnrequestPayload> {

    private static final Logger log = LoggerFactory.getLogger(ItemReceivedReturnrequestAction.class);

    @Override
    public void transitionTo(Returnrequest returnrequest,
                             ItemReceivedReturnrequestPayload payload,
                             State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Record warehouse receipt details
        if (payload.getWarehouseId() != null) {
            returnrequest.warehouseId = payload.getWarehouseId();
        }
        if (payload.getConditionOnReceipt() != null) {
            returnrequest.conditionOnReceipt = payload.getConditionOnReceipt();
        }

        returnrequest.getTransientMap().previousPayload = payload;
        log.info("Return request {} received at warehouse {}, condition: {}",
                returnrequest.getId(), returnrequest.warehouseId, returnrequest.conditionOnReceipt);
    }
}
