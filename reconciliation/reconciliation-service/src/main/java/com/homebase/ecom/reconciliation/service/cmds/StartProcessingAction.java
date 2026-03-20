package com.homebase.ecom.reconciliation.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.reconciliation.model.ReconciliationBatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the startProcessing event: CREATED -> PROCESSING.
 * Validates the batch has required fields and initiates the OWIZ pipeline.
 */
public class StartProcessingAction extends AbstractSTMTransitionAction<ReconciliationBatch, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(StartProcessingAction.class);

    @Override
    public void transitionTo(ReconciliationBatch batch, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (batch.getBatchDate() == null) {
            throw new IllegalArgumentException("Batch date is required to start processing");
        }
        if (batch.getGatewayType() == null || batch.getGatewayType().isEmpty()) {
            throw new IllegalArgumentException("Gateway type is required to start processing");
        }

        log.info("Starting reconciliation processing for batch {} (date: {}, gateway: {})",
                batch.getId(), batch.getBatchDate(), batch.getGatewayType());
    }
}
