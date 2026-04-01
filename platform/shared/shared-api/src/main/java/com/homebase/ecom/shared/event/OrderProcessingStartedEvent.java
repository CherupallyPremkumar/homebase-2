package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Event published when warehouse starts processing an order.
 * Consumed by: shipping BC to prepare for upcoming shipment.
 */
public class OrderProcessingStartedEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderId;
    private String warehouseId;
    private LocalDateTime startedAt;

    public OrderProcessingStartedEvent() {
    }

    public OrderProcessingStartedEvent(String orderId, String warehouseId, LocalDateTime startedAt) {
        this.orderId = orderId;
        this.warehouseId = warehouseId;
        this.startedAt = startedAt;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getWarehouseId() { return warehouseId; }
    public void setWarehouseId(String warehouseId) { this.warehouseId = warehouseId; }

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
}
