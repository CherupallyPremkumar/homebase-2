package com.homebase.ecom.disputes.service.postSaveHooks;

import com.homebase.ecom.disputes.model.Dispute;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PostSaveHook for RESOLVED state.
 * Logs the resolution outcome and notifies relevant parties.
 */
public class RESOLVEDDisputesPostSaveHook implements PostSaveHook<Dispute> {

    private static final Logger log = LoggerFactory.getLogger(RESOLVEDDisputesPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Dispute dispute, TransientMap map) {
        log.info("Dispute {} resolved. Outcome={}, refundAmount={}",
                dispute.getId(), dispute.resolutionOutcome, dispute.refundAmount);
    }
}
