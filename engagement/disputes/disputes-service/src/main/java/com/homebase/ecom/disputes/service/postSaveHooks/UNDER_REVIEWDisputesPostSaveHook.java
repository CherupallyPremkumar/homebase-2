package com.homebase.ecom.disputes.service.postSaveHooks;

import com.homebase.ecom.disputes.model.Dispute;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PostSaveHook for UNDER_REVIEW state.
 * Logs that the dispute is now under investigation by the support team.
 */
public class UNDER_REVIEWDisputesPostSaveHook implements PostSaveHook<Dispute> {

    private static final Logger log = LoggerFactory.getLogger(UNDER_REVIEWDisputesPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Dispute dispute, TransientMap map) {
        log.info("Dispute {} is now under review, assigned to {}",
                dispute.getId(), dispute.assignedTo);
    }
}
