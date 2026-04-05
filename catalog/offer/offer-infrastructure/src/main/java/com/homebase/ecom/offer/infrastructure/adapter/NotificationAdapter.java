package com.homebase.ecom.offer.infrastructure.adapter;

import com.homebase.ecom.offer.domain.port.NotificationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Infrastructure adapter for Notification BC integration.
 * In production, this would publish to notification.events topic or call Notification REST API.
 */
public class NotificationAdapter implements NotificationPort {

    private static final Logger log = LoggerFactory.getLogger(NotificationAdapter.class);

    @Override
    public void notifyOfferSuspended(String supplierId, String offerId, String productId, String reason) {
        log.info("NotificationPort: Notifying seller {} that offer {} for product {} was suspended. Reason: {}",
                supplierId, offerId, productId, reason);
    }

    @Override
    public void notifyOfferApproved(String supplierId, String offerId) {
        log.info("NotificationPort: Notifying seller {} that offer {} was approved", supplierId, offerId);
    }

    @Override
    public void notifyOfferRejected(String supplierId, String offerId, String reason) {
        log.info("NotificationPort: Notifying seller {} that offer {} was rejected. Reason: {}",
                supplierId, offerId, reason);
    }
}
