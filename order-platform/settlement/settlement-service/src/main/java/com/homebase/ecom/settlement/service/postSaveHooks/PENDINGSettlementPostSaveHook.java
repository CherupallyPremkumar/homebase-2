package com.homebase.ecom.settlement.service.postSaveHooks;

import com.homebase.ecom.settlement.model.Settlement;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post-save hook for PENDING state.
 * Logs settlement creation.
 */
public class PENDINGSettlementPostSaveHook implements PostSaveHook<Settlement> {

    private static final Logger log = LoggerFactory.getLogger(PENDINGSettlementPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Settlement settlement, TransientMap map) {
        log.info("Settlement {} in PENDING state for supplier {}",
                settlement.getId(), settlement.getSupplierId());
    }
}
