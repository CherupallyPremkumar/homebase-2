package com.homebase.ecom.fulfillment.service.postSaveHooks;

import com.homebase.ecom.fulfillment.model.FulfillmentSaga;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;

/**
 * Post-save hook executed when fulfillment saga enters INVENTORY_RESERVED state.
 */
public class INVENTORY_RESERVEDFulfillmentPostSaveHook implements PostSaveHook<FulfillmentSaga> {
    @Override
    public void execute(State startState, State endState, FulfillmentSaga saga, TransientMap map) {
    }
}
