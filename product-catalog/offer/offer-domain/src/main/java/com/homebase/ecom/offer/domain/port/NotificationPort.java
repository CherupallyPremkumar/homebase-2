package com.homebase.ecom.offer.domain.port;

/**
 * Port for sending notifications to sellers about offer state changes.
 */
public interface NotificationPort {

    /**
     * Notify seller that their offer has been suspended.
     */
    void notifyOfferSuspended(String supplierId, String offerId, String productId, String reason);

    /**
     * Notify seller that their offer has been approved.
     */
    void notifyOfferApproved(String supplierId, String offerId);

    /**
     * Notify seller that their offer has been rejected.
     */
    void notifyOfferRejected(String supplierId, String offerId, String reason);
}
