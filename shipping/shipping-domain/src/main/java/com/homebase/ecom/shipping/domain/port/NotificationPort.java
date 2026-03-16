package com.homebase.ecom.shipping.domain.port;

import com.homebase.ecom.shipping.model.Shipping;

/**
 * Port for sending shipping notifications to customers.
 * Adapter implementations connect to notification service (email, SMS, push).
 */
public interface NotificationPort {

    /**
     * Notifies customer that their shipment has been shipped.
     */
    void notifyShipped(Shipping shipping);

    /**
     * Notifies customer that their shipment is out for delivery.
     */
    void notifyOutForDelivery(Shipping shipping);

    /**
     * Notifies customer that their shipment has been delivered.
     */
    void notifyDelivered(Shipping shipping);

    /**
     * Notifies customer that a delivery attempt failed.
     */
    void notifyDeliveryFailed(Shipping shipping);

    /**
     * Notifies customer that their shipment has been returned to warehouse.
     */
    void notifyReturned(Shipping shipping);
}
