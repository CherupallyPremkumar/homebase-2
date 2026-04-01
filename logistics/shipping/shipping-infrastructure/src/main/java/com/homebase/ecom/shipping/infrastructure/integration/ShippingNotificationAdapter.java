package com.homebase.ecom.shipping.infrastructure.integration;

import com.homebase.ecom.shipping.domain.port.NotificationPort;
import com.homebase.ecom.shipping.model.Shipping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Infrastructure adapter for shipping notification operations.
 *
 * Currently a logging stub — will delegate to notification-client
 * once cross-service event wiring is complete.
 */
public class ShippingNotificationAdapter implements NotificationPort {

    private static final Logger log = LoggerFactory.getLogger(ShippingNotificationAdapter.class);

    @Override
    public void notifyShipped(Shipping shipping) {
        log.info("Notification: shipment shipped shipmentId={}, orderId={}",
                shipping.getId(), shipping.getOrderId());
    }

    @Override
    public void notifyOutForDelivery(Shipping shipping) {
        log.info("Notification: shipment out for delivery shipmentId={}, orderId={}",
                shipping.getId(), shipping.getOrderId());
    }

    @Override
    public void notifyDelivered(Shipping shipping) {
        log.info("Notification: shipment delivered shipmentId={}, orderId={}",
                shipping.getId(), shipping.getOrderId());
    }

    @Override
    public void notifyDeliveryFailed(Shipping shipping) {
        log.info("Notification: delivery attempt failed shipmentId={}, orderId={}",
                shipping.getId(), shipping.getOrderId());
    }

    @Override
    public void notifyReturned(Shipping shipping) {
        log.info("Notification: shipment returned to warehouse shipmentId={}, orderId={}",
                shipping.getId(), shipping.getOrderId());
    }
}
