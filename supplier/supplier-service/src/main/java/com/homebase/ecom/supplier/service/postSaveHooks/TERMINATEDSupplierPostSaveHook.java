package com.homebase.ecom.supplier.service.postSaveHooks;

import com.homebase.ecom.supplier.model.Supplier;
import com.homebase.ecom.supplier.domain.port.SupplierEventPublisher;
import com.homebase.ecom.supplier.domain.port.NotificationPort;
import com.homebase.ecom.shared.event.SupplierTerminatedEvent;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * Post-save hook for TERMINATED state.
 * Publishes SUPPLIER_TERMINATED event, ensures all products disabled, sends notification.
 */
public class TERMINATEDSupplierPostSaveHook implements PostSaveHook<Supplier> {

    private static final Logger log = LoggerFactory.getLogger(TERMINATEDSupplierPostSaveHook.class);

    @Autowired
    private SupplierEventPublisher supplierEventPublisher;

    @Autowired
    private NotificationPort notificationPort;

    @Override
    public void execute(State startState, State endState, Supplier supplier, TransientMap map) {
        log.info("PostSaveHook: Supplier '{}' (ID: {}) is now TERMINATED. Reason: {}. All products disabled.",
                supplier.getBusinessName(), supplier.getId(), supplier.getTerminationReason());

        // Publish SUPPLIER_TERMINATED event
        if (supplierEventPublisher != null) {
            SupplierTerminatedEvent event = new SupplierTerminatedEvent(
                    supplier.getId(), supplier.getTerminationReason(), LocalDateTime.now());
            supplierEventPublisher.publishSupplierTerminated(event);
        }

        // Send notification
        if (notificationPort != null) {
            notificationPort.notifySupplierTerminated(
                    supplier.getId(), supplier.getBusinessName(), supplier.getTerminationReason());
        }
    }
}
