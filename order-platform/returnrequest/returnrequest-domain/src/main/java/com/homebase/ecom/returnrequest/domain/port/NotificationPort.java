package com.homebase.ecom.returnrequest.domain.port;

/**
 * Port for sending return-related notifications to customers.
 * Adapter connects to the notification service.
 */
public interface NotificationPort {
    void notifyReturnApproved(String returnRequestId, String customerId, String orderId);
    void notifyReturnRejected(String returnRequestId, String customerId, String orderId, String reason);
    void notifyRefundInitiated(String returnRequestId, String customerId, String orderId, String amount);
    void notifyReturnCompleted(String returnRequestId, String customerId, String orderId);
}
