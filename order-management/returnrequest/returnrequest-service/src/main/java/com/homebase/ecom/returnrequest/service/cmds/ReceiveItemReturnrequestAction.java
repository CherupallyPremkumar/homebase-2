package com.homebase.ecom.returnrequest.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.returnrequest.model.Returnrequest;
import com.homebase.ecom.returnrequest.dto.ReceiveItemReturnrequestPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the receiveItem transition (APPROVED/PARTIALLY_APPROVED -> ITEM_RECEIVED).
 * Warehouse confirms receipt and records condition.
 */
public class ReceiveItemReturnrequestAction extends AbstractSTMTransitionAction<Returnrequest,
        ReceiveItemReturnrequestPayload> {

    private static final Logger log = LoggerFactory.getLogger(ReceiveItemReturnrequestAction.class);

    @Override
    public void transitionTo(Returnrequest returnrequest,
                             ReceiveItemReturnrequestPayload payload,
                             State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

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
