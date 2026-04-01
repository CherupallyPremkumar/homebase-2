package com.homebase.ecom.inventory.infrastructure.persistence.entity;

import java.time.Instant;
import com.homebase.ecom.inventory.domain.model.ReservationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class InventoryReservationEntity {
    @Column(name = "order_id")
    private String orderId;
    
    @Column(name = "session_id")
    private String sessionId;
    
    @Column(name = "quantity")
    private Integer quantity;
    
    @Column(name = "reserved_at")
    private Instant reservedAt;
    
    @Column(name = "expires_at")
    private Instant expiresAt;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReservationStatus status;

    // Getters and Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Instant getReservedAt() { return reservedAt; }
    public void setReservedAt(Instant reservedAt) { this.reservedAt = reservedAt; }

    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }

    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }
}
