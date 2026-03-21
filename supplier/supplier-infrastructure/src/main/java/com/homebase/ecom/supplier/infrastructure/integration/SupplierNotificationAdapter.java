package com.homebase.ecom.supplier.infrastructure.integration;

import com.homebase.ecom.supplier.domain.port.NotificationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Infrastructure adapter for supplier notification operations.
 *
 * Currently a logging stub — will delegate to notification-client
 * once cross-service event wiring is complete.
 */
public class SupplierNotificationAdapter implements NotificationPort {

    private static final Logger log = LoggerFactory.getLogger(SupplierNotificationAdapter.class);

    @Override
    public void notifySupplierApproved(String supplierId, String businessName, String contactEmail) {
        log.info("Notification: supplier approved supplierId={}, businessName={}, contactEmail={}",
                supplierId, businessName, contactEmail);
    }

    @Override
    public void notifySupplierSuspended(String supplierId, String businessName, String reason) {
        log.info("Notification: supplier suspended supplierId={}, businessName={}, reason={}",
                supplierId, businessName, reason);
    }

    @Override
    public void notifySupplierTerminated(String supplierId, String businessName, String reason) {
        log.info("Notification: supplier terminated supplierId={}, businessName={}, reason={}",
                supplierId, businessName, reason);
    }

    @Override
    public void notifySupplierOnProbation(String supplierId, String businessName, String reason) {
        log.info("Notification: supplier on probation supplierId={}, businessName={}, reason={}",
                supplierId, businessName, reason);
    }
}
