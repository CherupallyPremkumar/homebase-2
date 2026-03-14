package com.homebase.ecom.checkout.api.dto;

import java.time.LocalDateTime;

/**
 * Locks Response DTO
 */
public class LocksResponseDTO {

    private String cartLockToken;
    private String priceLockToken;
    private String inventoryReservationId;
    private LocalDateTime expiresAt;

    // Getters and Setters
    public String getCartLockToken() {
        return cartLockToken;
    }

    public void setCartLockToken(String cartLockToken) {
        this.cartLockToken = cartLockToken;
    }

    public String getPriceLockToken() {
        return priceLockToken;
    }

    public void setPriceLockToken(String priceLockToken) {
        this.priceLockToken = priceLockToken;
    }

    public String getInventoryReservationId() {
        return inventoryReservationId;
    }

    public void setInventoryReservationId(String inventoryReservationId) {
        this.inventoryReservationId = inventoryReservationId;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
