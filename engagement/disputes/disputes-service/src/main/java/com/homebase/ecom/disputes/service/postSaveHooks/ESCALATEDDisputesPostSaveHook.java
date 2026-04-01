package com.homebase.ecom.disputes.service.postSaveHooks;

import com.homebase.ecom.disputes.model.Dispute;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PostSaveHook for ESCALATED state.
 * Logs the escalation for supervisor attention.
 */
public class ESCALATEDDisputesPostSaveHook implements PostSaveHook<Dispute> {

    private static final Logger log = LoggerFactory.getLogger(ESCALATEDDisputesPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Dispute dispute, TransientMap map) {
        log.info("Dispute {} escalated from {} to ESCALATED, priority={}",
                dispute.getId(), startState.getId(), dispute.priority);
    }
}
