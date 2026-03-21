package com.homebase.ecom.supplier.service.postSaveHooks;

import com.homebase.ecom.supplier.model.Supplier;
import com.homebase.ecom.supplier.domain.port.NotificationPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Post-save hook for ON_PROBATION state.
 * Sends probation notification to the supplier.
 * Products remain enabled during probation but supplier is under performance monitoring.
 */
public class ON_PROBATIONSupplierPostSaveHook implements PostSaveHook<Supplier> {

    private static final Logger log = LoggerFactory.getLogger(ON_PROBATIONSupplierPostSaveHook.class);

    @Autowired
    private NotificationPort notificationPort;

    @Override
    public void execute(State startState, State endState, Supplier supplier, TransientMap map) {
        log.info("PostSaveHook: Supplier '{}' (ID: {}) is now ON_PROBATION. Reason: {}",
                supplier.getBusinessName(), supplier.getId(), supplier.getProbationReason());

        // Send probation warning notification
        if (notificationPort != null) {
            notificationPort.notifySupplierOnProbation(
                    supplier.getId(), supplier.getBusinessName(), supplier.getProbationReason());
        }
    }
}
