package com.homebase.ecom.supplier.service.postSaveHooks;

import com.homebase.ecom.supplier.model.Supplier;
import com.homebase.ecom.supplier.domain.port.SupplierEventPublisher;
import com.homebase.ecom.supplier.domain.port.NotificationPort;
import com.homebase.ecom.shared.event.SupplierApprovedEvent;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * Post-save hook for APPROVED state.
 * Publishes SUPPLIER_APPROVED event and sends notification.
 * Enables product listing for the supplier.
 */
public class APPROVEDSupplierPostSaveHook implements PostSaveHook<Supplier> {

    private static final Logger log = LoggerFactory.getLogger(APPROVEDSupplierPostSaveHook.class);

    @Autowired
    private SupplierEventPublisher supplierEventPublisher;

    @Autowired
    private NotificationPort notificationPort;

    @Override
    public void execute(State startState, State endState, Supplier supplier, TransientMap map) {
        log.info("PostSaveHook: Supplier '{}' (ID: {}) is now APPROVED",
                supplier.getBusinessName(), supplier.getId());

        // Publish SUPPLIER_APPROVED event to supplier.events topic
        if (supplierEventPublisher != null) {
            SupplierApprovedEvent event = new SupplierApprovedEvent(
                    supplier.getId(), supplier.getBusinessName(), LocalDateTime.now());
            supplierEventPublisher.publishSupplierApproved(event);
        }

        // Send notification
        if (notificationPort != null) {
            notificationPort.notifySupplierApproved(
                    supplier.getId(), supplier.getBusinessName(), supplier.getContactEmail());
        }
    }
}
