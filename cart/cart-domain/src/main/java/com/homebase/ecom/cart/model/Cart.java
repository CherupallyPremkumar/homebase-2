package com.homebase.ecom.cart.model;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import com.homebase.ecom.shared.Money;
import org.chenile.workflow.model.*;
import org.chenile.utils.entity.model.AbstractExtendedStateEntity;

public class Cart extends AbstractExtendedStateEntity implements ActivityEnabledStateEntity, ContainsTransientMap {

    private String userId;

    private List<CartItem> items = new ArrayList<>();

    private Money totalAmount;

    private String shippingAddress;

    private String billingAddress;

    private String appliedPromoCode;

    private Money discountAmount;

    private Money taxAmount;

    public String description;

    @Override
    public org.chenile.stm.State getCurrentState() {
        return super.getCurrentState();
    }

    public TransientMap transientMap = new TransientMap();

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getAppliedPromoCode() {
        return appliedPromoCode;
    }

    public void setAppliedPromoCode(String appliedPromoCode) {
        this.appliedPromoCode = appliedPromoCode;
    }

    public Money getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Money discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Money totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Money getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Money taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    /**
     * Adds an item to the cart. If the product already exists, increments quantity.
     * Recalculates the cart total after modification.
     */
    public void addItem(CartItem item) {
        Optional<CartItem> existing = items.stream()
                .filter(i -> i.getProductId().equals(item.getProductId()))
                .findFirst();
        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + item.getQuantity());
        } else {
            items.add(item);
        }
        recalculateTotal();
    }

    /**
     * Removes an item from the cart by productId and recalculates the total.
     */
    public void removeItem(String productId) {
        items.removeIf(i -> i.getProductId().equals(productId));
        recalculateTotal();
    }

    /**
     * Updates the quantity of an existing item and recalculates the total.
     */
    public void updateItemQuantity(String productId, int newQuantity) {
        items.stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(newQuantity);
                    recalculateTotal();
                });
    }

    /**
     * Returns true if any item in the cart is not AVAILABLE.
     */
    public boolean hasUnavailableItems() {
        return items.stream().anyMatch(i -> i.getStatus() != CartItemStatus.AVAILABLE);
    }

    /**
     * Snapshots current prices by recording a freeze timestamp in the description.
     */
    public void freezePrices() {
        this.description = "Prices frozen at checkout: " + Instant.now();
    }

    /**
     * Recalculates the cart total from available items, applying any active discount.
     * Uses the currency from the existing totalAmount or the first item's price.
     */
    public void recalculateTotal() {
        BigDecimal itemsTotal = BigDecimal.ZERO;
        String currency = resolveCurrency();

        for (CartItem item : items) {
            if (item.getStatus() == CartItemStatus.AVAILABLE
                    && item.getPrice() != null
                    && item.getPrice().getAmount() != null) {
                itemsTotal = itemsTotal.add(
                        item.getPrice().getAmount().multiply(BigDecimal.valueOf(item.getQuantity())));
            }
        }

        BigDecimal finalTotal = itemsTotal;

        // Apply discount if promo code is active
        if (this.discountAmount != null
                && this.discountAmount.getAmount() != null
                && this.discountAmount.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            finalTotal = finalTotal.subtract(this.discountAmount.getAmount());
            if (finalTotal.compareTo(BigDecimal.ZERO) < 0) {
                finalTotal = BigDecimal.ZERO;
            }
        }

        this.totalAmount = new Money(finalTotal, currency);
    }

    /**
     * Resolves the currency to use for the cart total.
     */
    private String resolveCurrency() {
        if (this.totalAmount != null && this.totalAmount.getCurrency() != null) {
            return this.totalAmount.getCurrency();
        }
        // Fall back to first item's currency
        for (CartItem item : items) {
            if (item.getPrice() != null && item.getPrice().getCurrency() != null) {
                return item.getPrice().getCurrency();
            }
        }
        return "INR"; // Default currency
    }

    public TransientMap getTransientMap() {
        return this.transientMap;
    }

    public void setTransientMap(TransientMap transientMap) {
        this.transientMap = transientMap;
    }

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
