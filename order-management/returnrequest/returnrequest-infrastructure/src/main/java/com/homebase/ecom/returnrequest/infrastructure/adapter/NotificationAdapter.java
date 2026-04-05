package com.homebase.ecom.returnrequest.infrastructure.adapter;

import com.homebase.ecom.returnrequest.domain.port.NotificationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adapter for the NotificationPort. In production, this publishes events
 * to the notification service to send customer notifications.
 */
public class NotificationAdapter implements NotificationPort {

    private static final Logger log = LoggerFactory.getLogger(NotificationAdapter.class);

    @Override
    public void notifyReturnApproved(String returnRequestId, String customerId, String orderId) {
        log.info("Notification: Return {} approved for customer {} (order {})",
                returnRequestId, customerId, orderId);
    }

    @Override
    public void notifyReturnRejected(String returnRequestId, String customerId, String orderId, String reason) {
        log.info("Notification: Return {} rejected for customer {} (order {}): {}",
                returnRequestId, customerId, orderId, reason);
    }

    @Override
    public void notifyRefundInitiated(String returnRequestId, String customerId, String orderId, String amount) {
        log.info("Notification: Refund of {} initiated for return {} (customer {}, order {})",
                amount, returnRequestId, customerId, orderId);
    }

    @Override
    public void notifyReturnCompleted(String returnRequestId, String customerId, String orderId) {
        log.info("Notification: Return {} completed for customer {} (order {})",
                returnRequestId, customerId, orderId);
    }
}
