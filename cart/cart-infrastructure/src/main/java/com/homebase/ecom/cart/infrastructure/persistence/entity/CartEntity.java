package com.homebase.ecom.cart.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
public class CartEntity extends AbstractJpaStateEntity {

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "session_id")
    private String sessionId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cart", orphanRemoval = true)
    private List<CartItemEntity> items = new ArrayList<>();

    @Column(name = "subtotal")
    private long subtotal;

    @Column(name = "currency", length = 3)
    private String currency = "INR";

    @Column(name = "coupon_codes", length = 500)
    private String couponCodes;

    @Column(name = "discount_amount")
    private long discountAmount;

    @Column(name = "total")
    private long total;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "description")
    public String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private List<CartActivityLogEntity> activities = new ArrayList<>();

    // ── Getters & Setters ──────────────────────────────────────────────

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<CartItemEntity> getItems() {
        return items;
    }

    public void setItems(List<CartItemEntity> items) {
        this.items = items;
    }

    public long getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(long subtotal) {
        this.subtotal = subtotal;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCouponCodes() {
        return couponCodes;
    }

    public void setCouponCodes(String couponCodes) {
        this.couponCodes = couponCodes;
    }

    public long getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(long discountAmount) {
        this.discountAmount = discountAmount;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public List<CartActivityLogEntity> getActivities() {
        return activities;
    }

    public void setActivities(List<CartActivityLogEntity> activities) {
        this.activities = activities;
    }
}
