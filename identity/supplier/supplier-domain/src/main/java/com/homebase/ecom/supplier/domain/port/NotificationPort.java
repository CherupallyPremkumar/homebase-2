package com.homebase.ecom.supplier.domain.port;

/**
 * Outbound Port (Hexagonal): Notification delivery.
 * Domain depends on this interface. Notification infrastructure provides the adapter.
 */
public interface NotificationPort {

    /**
     * Sends supplier approval notification (email, SMS, etc.).
     */
    void notifySupplierApproved(String supplierId, String businessName, String contactEmail);

    /**
     * Sends supplier suspension notification with reason.
     */
    void notifySupplierSuspended(String supplierId, String businessName, String reason);

    /**
     * Sends supplier termination notification with reason.
     */
    void notifySupplierTerminated(String supplierId, String businessName, String reason);

    /**
     * Sends probation warning notification.
     */
    void notifySupplierOnProbation(String supplierId, String businessName, String reason);
}
