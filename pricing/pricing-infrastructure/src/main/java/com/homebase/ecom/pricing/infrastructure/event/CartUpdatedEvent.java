package com.homebase.ecom.pricing.infrastructure.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class CartUpdatedEvent {
    private String eventId;
    private String cartId;
    private String userId;
    private LocalDateTime occurredAt;
    private List<Object> items; // Simplified
    private BigDecimal subtotal;
    private int totalQuantity;
    private String eventType;

    public CartUpdatedEvent() {}

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public String getCartId() { return cartId; }
    public void setCartId(String cartId) { this.cartId = cartId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public LocalDateTime getOccurredAt() { return occurredAt; }
    public void setOccurredAt(LocalDateTime occurredAt) { this.occurredAt = occurredAt; }
    public List<Object> getItems() { return items; }
    public void setItems(List<Object> items) { this.items = items; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartUpdatedEvent that = (CartUpdatedEvent) o;
        return totalQuantity == that.totalQuantity &&
                Objects.equals(eventId, that.eventId) &&
                Objects.equals(cartId, that.cartId) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(occurredAt, that.occurredAt) &&
                Objects.equals(items, that.items) &&
                Objects.equals(subtotal, that.subtotal) &&
                Objects.equals(eventType, that.eventType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, cartId, userId, occurredAt, items, subtotal, totalQuantity, eventType);
    }

    @Override
    public String toString() {
        return "CartUpdatedEvent{" +
                "eventId='" + eventId + '\'' +
                ", cartId='" + cartId + '\'' +
                ", userId='" + userId + '\'' +
                ", occurredAt=" + occurredAt +
                ", items=" + items +
                ", subtotal=" + subtotal +
                ", totalQuantity=" + totalQuantity +
                ", eventType='" + eventType + '\'' +
                '}';
    }
}
