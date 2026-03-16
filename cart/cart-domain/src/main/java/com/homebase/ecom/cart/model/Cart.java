package com.homebase.ecom.cart.model;

import com.homebase.ecom.shared.Money;
import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import java.time.LocalDateTime;
import java.util.*;
import org.chenile.workflow.model.*;
import org.chenile.utils.entity.model.AbstractExtendedStateEntity;

/**
 * Cart bounded context domain model.
 *
 * All money fields use the Money value object which stores amounts
 * in the smallest currency unit (paise for INR, cents for USD).
 */
public class Cart extends AbstractExtendedStateEntity implements ActivityEnabledStateEntity, ContainsTransientMap {

    private String customerId;
    private String sessionId;
    private List<CartItem> items = new ArrayList<>();
    private Money subtotal = Money.ZERO_INR;
    private List<String> couponCodes = new ArrayList<>();
    private Money discountAmount = Money.ZERO_INR;
    private Money total = Money.ZERO_INR;
    private LocalDateTime expiresAt;
    private String notes;

    public String description;

    @Override
    public org.chenile.stm.State getCurrentState() {
        return super.getCurrentState();
    }

    public TransientMap transientMap = new TransientMap();

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

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public Money getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Money subtotal) {
        this.subtotal = subtotal;
    }

    public String getCurrency() {
        return subtotal.getCurrency();
    }

    public List<String> getCouponCodes() {
        return couponCodes;
    }

    public void setCouponCodes(List<String> couponCodes) {
        this.couponCodes = couponCodes;
    }

    public Money getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Money discountAmount) {
        this.discountAmount = discountAmount;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Money getTotal() {
        return total;
    }

    public void setTotal(Money total) {
        this.total = total;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // ── Domain logic ───────────────────────────────────────────────────

    /**
     * Adds an item to the cart. If the same variantId already exists, increments quantity.
     * variantId is the unique key for cart line items.
     * Note: pricing is NOT recalculated here — caller must call recalculatePricing() via PricingPort.
     */
    public void addItem(CartItem item) {
        Optional<CartItem> existing = items.stream()
                .filter(i -> i.getVariantId().equals(item.getVariantId()))
                .findFirst();
        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + item.getQuantity());
        } else {
            items.add(item);
        }
    }

    /**
     * Removes an item from the cart by variantId.
     * Note: pricing is NOT recalculated here — caller must call recalculatePricing() via PricingPort.
     */
    public void removeItem(String variantId) {
        items.removeIf(i -> i.getVariantId().equals(variantId));
    }

    /**
     * Updates the quantity of an existing item by variantId.
     * Note: pricing is NOT recalculated here — caller must call recalculatePricing() via PricingPort.
     */
    public void updateItemQuantity(String variantId, int newQuantity) {
        items.stream()
                .filter(i -> i.getVariantId().equals(variantId))
                .findFirst()
                .ifPresent(item -> item.setQuantity(newQuantity));
    }

    /**
     * Adds a coupon code to the cart.
     */
    public void addCouponCode(String couponCode) {
        if (!couponCodes.contains(couponCode)) {
            couponCodes.add(couponCode);
        }
    }

    /**
     * Removes a coupon code from the cart.
     */
    public void removeCouponCode(String couponCode) {
        couponCodes.remove(couponCode);
    }

    /**
     * Local fallback: recalculates line totals and subtotal from items.
     * In production, Pricing Service computes ALL values (subtotal, discount, total, line totals).
     * This method is only used as a fallback when Pricing Service is unavailable (e.g. unit tests).
     */
    public void recalculateSubtotal() {
        String curr = subtotal.getCurrency();
        Money sum = Money.zero(curr);
        for (CartItem item : items) {
            item.recalculateLineTotal();
            sum = sum.add(item.getLineTotal());
        }
        this.subtotal = sum;
    }

    public TransientMap getTransientMap() {
        return this.transientMap;
    }

    public void setTransientMap(TransientMap transientMap) {
        this.transientMap = transientMap;
    }

    // ── Activity support ───────────────────────────────────────────────

    public List<CartActivityLog> activities = new ArrayList<>();

    @Override
    public Collection<ActivityLog> obtainActivities() {
        Collection<ActivityLog> acts = new ArrayList<>();
        for (ActivityLog a : activities) {
            acts.add(a);
        }
        return acts;
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        CartActivityLog activityLog = new CartActivityLog();
        activityLog.activityName = eventId;
        activityLog.activityComment = comment;
        activityLog.activitySuccess = true;
        activities.add(activityLog);
        return activityLog;
    }
}
