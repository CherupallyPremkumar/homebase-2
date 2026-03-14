package com.homebase.ecom.supplier.service.postSaveHooks;

import com.homebase.ecom.supplier.model.Supplier;
import com.homebase.ecom.supplier.domain.port.SupplierEventPublisher;
import com.homebase.ecom.shared.event.SupplierActivatedEvent;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * Post-save hook for ACTIVE state.
 * Publishes SupplierActivatedEvent when a supplier transitions to ACTIVE.
 */
public class ACTIVESupplierPostSaveHook implements PostSaveHook<Supplier> {

    private static final Logger log = LoggerFactory.getLogger(ACTIVESupplierPostSaveHook.class);

    @Autowired(required = false)
    private SupplierEventPublisher supplierEventPublisher;

    @Override
    public void execute(State startState, State endState, Supplier supplier, TransientMap map) {
        log.info("PostSaveHook: Supplier '{}' (ID: {}) is now ACTIVE",
                supplier.getName(), supplier.getId());

        if (supplierEventPublisher != null) {
            SupplierActivatedEvent event = new SupplierActivatedEvent(
                    supplier.getId(), supplier.getName(), LocalDateTime.now());
            supplierEventPublisher.publishSupplierActivated(event);
        }
    }
}
