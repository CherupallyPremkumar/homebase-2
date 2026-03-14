package com.homebase.ecom.supplier.service.postSaveHooks;

import com.homebase.ecom.supplier.model.Supplier;
import com.homebase.ecom.supplier.domain.port.SupplierEventPublisher;
import com.homebase.ecom.shared.event.SupplierSuspendedEvent;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * Post-save hook for SUSPENDED state.
 * Publishes SupplierSuspendedEvent when a supplier is suspended.
 */
public class SUSPENDEDSupplierPostSaveHook implements PostSaveHook<Supplier> {

    private static final Logger log = LoggerFactory.getLogger(SUSPENDEDSupplierPostSaveHook.class);

    @Autowired(required = false)
    private SupplierEventPublisher supplierEventPublisher;

    @Override
    public void execute(State startState, State endState, Supplier supplier, TransientMap map) {
        log.info("PostSaveHook: Supplier '{}' (ID: {}) is now SUSPENDED. Reason: {}",
                supplier.getName(), supplier.getId(), supplier.getSuspensionReason());

        if (supplierEventPublisher != null) {
            SupplierSuspendedEvent event = new SupplierSuspendedEvent(
                    supplier.getId(), supplier.getSuspensionReason(), LocalDateTime.now());
            supplierEventPublisher.publishSupplierSuspended(event);
        }
    }
}
