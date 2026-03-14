package com.homebase.ecom.checkout.domain.model;

import java.util.UUID;

public class OrderDetails {
    private final UUID orderId;
    private final String orderNumber;
    private final String status;

    public OrderDetails(UUID orderId, String orderNumber, String status) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.status = status;
    }

    public UUID getOrderId() { return orderId; }
    public String getOrderNumber() { return orderNumber; }
    public String getStatus() { return status; }
}
