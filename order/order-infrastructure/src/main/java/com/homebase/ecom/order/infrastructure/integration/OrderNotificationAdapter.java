package com.homebase.ecom.order.infrastructure.integration;

import com.homebase.ecom.order.port.NotificationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Infrastructure adapter for notification operations.
 *
 * Currently a logging stub — will delegate to notification-client
 * once cross-service event wiring is complete.
 */
public class OrderNotificationAdapter implements NotificationPort {

    private static final Logger log = LoggerFactory.getLogger(OrderNotificationAdapter.class);

    @Override
    public void notifyOrderCreated(String orderId, String customerId) {
        log.info("Notification: order created orderId={}, customerId={}", orderId, customerId);
    }

    @Override
    public void notifyOrderShipped(String orderId, String customerId, String trackingNumber) {
        log.info("Notification: order shipped orderId={}, customerId={}, tracking={}",
                orderId, customerId, trackingNumber);
    }

    @Override
    public void notifyOrderDelivered(String orderId, String customerId) {
        log.info("Notification: order delivered orderId={}, customerId={}", orderId, customerId);
    }

    @Override
    public void notifyOrderCancelled(String orderId, String customerId, String reason) {
        log.info("Notification: order cancelled orderId={}, customerId={}, reason={}",
                orderId, customerId, reason);
    }
}
