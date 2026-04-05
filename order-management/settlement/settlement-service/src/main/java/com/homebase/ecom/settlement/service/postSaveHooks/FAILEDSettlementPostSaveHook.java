package com.homebase.ecom.settlement.service.postSaveHooks;

import com.homebase.ecom.settlement.model.Settlement;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post-save hook for FAILED state.
 */
public class FAILEDSettlementPostSaveHook implements PostSaveHook<Settlement> {

    private static final Logger log = LoggerFactory.getLogger(FAILEDSettlementPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Settlement settlement, TransientMap map) {
        log.warn("Settlement {} FAILED for supplier {}. Previous state: {}",
                settlement.getId(), settlement.getSupplierId(),
                startState != null ? startState.getStateId() : "NONE");
    }
}
