package com.homebase.ecom.order.port;

/**
 * Domain port for fulfillment operations.
 * Infrastructure layer provides the adapter (calls fulfillment BC).
 */
public interface FulfillmentPort {
    /**
     * Trigger fulfillment for a paid order.
     */
    void triggerFulfillment(String orderId);

    /**
     * Cancel fulfillment for a cancelled order.
     */
    void cancelFulfillment(String orderId);
}
