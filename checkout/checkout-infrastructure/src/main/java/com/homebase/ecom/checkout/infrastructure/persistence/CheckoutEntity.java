package com.homebase.ecom.checkout.infrastructure.persistence;

import com.homebase.ecom.checkout.domain.model.CheckoutState;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "checkouts")
public class CheckoutEntity {
    @Id
    private UUID checkoutId;
    private UUID cartId;
    private UUID userId;
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    private CheckoutState state;

    @Column(name = "idempotency_key", unique = true)
    private String idempotencyKey;

    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    @Column(columnDefinition = "TEXT")
    private String lockedCartJson;
    @Column(columnDefinition = "TEXT")
    private String lockedPriceJson;
    @Column(columnDefinition = "TEXT")
    private String orderDetailsJson;
    @Column(columnDefinition = "TEXT")
    private String paymentDetailsJson;

    // Manual Getters and Setters
    public UUID getCheckoutId() { return checkoutId; }
    public void setCheckoutId(UUID checkoutId) { this.checkoutId = checkoutId; }

    public UUID getCartId() { return cartId; }
    public void setCartId(UUID cartId) { this.cartId = cartId; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }

    public CheckoutState getState() { return state; }
    public void setState(CheckoutState state) { this.state = state; }

    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public String getLockedCartJson() { return lockedCartJson; }
    public void setLockedCartJson(String lockedCartJson) { this.lockedCartJson = lockedCartJson; }

    public String getLockedPriceJson() { return lockedPriceJson; }
    public void setLockedPriceJson(String lockedPriceJson) { this.lockedPriceJson = lockedPriceJson; }

    public String getOrderDetailsJson() { return orderDetailsJson; }
    public void setOrderDetailsJson(String orderDetailsJson) { this.orderDetailsJson = orderDetailsJson; }

    public String getPaymentDetailsJson() { return paymentDetailsJson; }
    public void setPaymentDetailsJson(String paymentDetailsJson) { this.paymentDetailsJson = paymentDetailsJson; }
}
