package com.homebase.ecom.supplier.service.postSaveHooks;

import com.homebase.ecom.supplier.model.Supplier;
import com.homebase.ecom.supplier.domain.port.SupplierEventPublisher;
import com.homebase.ecom.shared.event.SupplierBlacklistedEvent;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * Post-save hook for BLACKLISTED state.
 * Publishes SupplierBlacklistedEvent when a supplier is permanently blacklisted.
 * All products should be disabled and all pending payouts should be held.
 */
public class BLACKLISTEDSupplierPostSaveHook implements PostSaveHook<Supplier> {

    private static final Logger log = LoggerFactory.getLogger(BLACKLISTEDSupplierPostSaveHook.class);

    @Autowired(required = false)
    private SupplierEventPublisher supplierEventPublisher;

    @Override
    public void execute(State startState, State endState, Supplier supplier, TransientMap map) {
        log.info("PostSaveHook: Supplier '{}' (ID: {}) is now BLACKLISTED. Reason: {}. All products disabled.",
                supplier.getName(), supplier.getId(), supplier.getBlacklistReason());

        if (supplierEventPublisher != null) {
            SupplierBlacklistedEvent event = new SupplierBlacklistedEvent(
                    supplier.getId(), supplier.getBlacklistReason(), LocalDateTime.now());
            supplierEventPublisher.publishSupplierBlacklisted(event);
        }
    }
}
