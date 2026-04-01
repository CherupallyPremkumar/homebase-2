package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Event published when items are picked from warehouse shelves.
 */
public class OrderItemsPickedEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderId;
    private int itemCount;
    private LocalDateTime pickedAt;

    public OrderItemsPickedEvent() {
    }

    public OrderItemsPickedEvent(String orderId, int itemCount, LocalDateTime pickedAt) {
        this.orderId = orderId;
        this.itemCount = itemCount;
        this.pickedAt = pickedAt;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public int getItemCount() { return itemCount; }
    public void setItemCount(int itemCount) { this.itemCount = itemCount; }

    public LocalDateTime getPickedAt() { return pickedAt; }
    public void setPickedAt(LocalDateTime pickedAt) { this.pickedAt = pickedAt; }
}
