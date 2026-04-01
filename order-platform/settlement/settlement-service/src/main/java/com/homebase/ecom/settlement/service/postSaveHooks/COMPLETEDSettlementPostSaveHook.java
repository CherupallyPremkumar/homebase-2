package com.homebase.ecom.settlement.service.postSaveHooks;

import com.homebase.ecom.settlement.model.Settlement;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post-save hook for COMPLETED state (terminal success).
 */
public class COMPLETEDSettlementPostSaveHook implements PostSaveHook<Settlement> {

    private static final Logger log = LoggerFactory.getLogger(COMPLETEDSettlementPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Settlement settlement, TransientMap map) {
        log.info("Settlement {} COMPLETED for supplier {}. Disbursement ref: {}",
                settlement.getId(), settlement.getSupplierId(), settlement.getDisbursementReference());
    }
}
