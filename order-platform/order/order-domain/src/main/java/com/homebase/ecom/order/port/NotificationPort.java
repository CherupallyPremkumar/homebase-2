package com.homebase.ecom.order.port;

/**
 * Domain port for notification operations.
 * Infrastructure layer provides the adapter (calls notification BC).
 */
public interface NotificationPort {
    void notifyOrderCreated(String orderId, String customerId);
    void notifyOrderShipped(String orderId, String customerId, String trackingNumber);
    void notifyOrderDelivered(String orderId, String customerId);
    void notifyOrderCancelled(String orderId, String customerId, String reason);
}
