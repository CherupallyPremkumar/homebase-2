package com.homebase.ecom.order.model;

public enum OrderStatus {
    PENDING,
    PAID,
    PROCESSING,
    SHIPPED,
    COURIER_PICKUP,
    DELIVERED,
    CANCELLED,
    RETURN_REQUESTED,
    RETURN_APPROVED,
    RETURN_REJECTED,
    REFUND_INITIATED,
    REFUND_COMPLETED
}
