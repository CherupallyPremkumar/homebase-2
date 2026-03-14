package com.homebase.ecom.order.model;

/**
 * Defines the granular lifecycle states of an individual item within an order.
 */
public enum OrderItemStatus {
    /**
     * Item has been placed as part of a new order.
     */
    PLACED,

    /**
     * User has requested cancellation of this specific item.
     */
    CANCELLATION_REQUESTED,

    /**
     * Item has been successfully cancelled (before shipment).
     */
    CANCELLED,

    /**
     * Business/User has initiated a refund for this item.
     */
    REFUND_REQUESTED,

    /**
     * Gateway has confirmed the refund for this item.
     */
    REFUNDED,

    /**
     * User has requested to return the item after delivery.
     */
    RETURN_REQUESTED,

    /**
     * Hub has received and verified the returned item.
     */
    RETURN_RECEIVED
}
