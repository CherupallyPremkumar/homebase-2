package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Event published when a courier picks up the order.
 * Consumed by: shipping BC to create shipment tracking record.
 */
public class OrderShippedEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderId;
    private String carrier;
    private String trackingNumber;
    private LocalDateTime estimatedDelivery;
    private LocalDateTime shippedAt;

    public OrderShippedEvent() {
    }

    public OrderShippedEvent(String orderId, String carrier, String trackingNumber,
            LocalDateTime estimatedDelivery, LocalDateTime shippedAt) {
        this.orderId = orderId;
        this.carrier = carrier;
        this.trackingNumber = trackingNumber;
        this.estimatedDelivery = estimatedDelivery;
        this.shippedAt = shippedAt;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCarrier() { return carrier; }
    public void setCarrier(String carrier) { this.carrier = carrier; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public LocalDateTime getEstimatedDelivery() { return estimatedDelivery; }
    public void setEstimatedDelivery(LocalDateTime estimatedDelivery) { this.estimatedDelivery = estimatedDelivery; }

    public LocalDateTime getShippedAt() { return shippedAt; }
    public void setShippedAt(LocalDateTime shippedAt) { this.shippedAt = shippedAt; }
}
