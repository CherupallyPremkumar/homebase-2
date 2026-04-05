package com.homebase.ecom.pricing.domain.model;

import com.homebase.ecom.shared.Money;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OWIZ shared context — flows through all pricing pipeline commands.
 * Each command reads/writes to this context as it executes.
 */
public class PricingContext {

    // ── Input (set before pipeline runs) ──
    private String cartId;
    private String userId;
    private String currency;
    private List<LineItemPricing> lineItems = new ArrayList<>();
    private List<String> couponCodes = new ArrayList<>();
    private String region;
    private String customerTier;

    // ── Intermediate (set by commands as they execute) ──
    private List<AppliedPromotion> appliedPromotions = new ArrayList<>();
    private Money subtotal;
    private Money totalDiscount;
    private Money taxAmount;
    private Money shippingCost;

    // ── Pipeline control ──
    private Map<String, Boolean> skipSteps = new HashMap<>();

    // ── Output (read after pipeline completes) ──
    private PriceBreakdown priceBreakdown;
    private boolean error;
    private String errorMessage;

    public PricingContext() {}

    public PricingContext(String cartId, String userId, String currency) {
        this.cartId = cartId;
        this.userId = userId;
        this.currency = currency;
    }

    // ── Input getters/setters ──

    public String getCartId() { return cartId; }
    public void setCartId(String cartId) { this.cartId = cartId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public List<LineItemPricing> getLineItems() { return lineItems; }
    public void setLineItems(List<LineItemPricing> lineItems) { this.lineItems = lineItems; }
    public List<String> getCouponCodes() { return couponCodes; }
    public void setCouponCodes(List<String> couponCodes) { this.couponCodes = couponCodes; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getCustomerTier() { return customerTier; }
    public void setCustomerTier(String customerTier) { this.customerTier = customerTier; }

    // ── Intermediate getters/setters ──

    public List<AppliedPromotion> getAppliedPromotions() { return appliedPromotions; }
    public void setAppliedPromotions(List<AppliedPromotion> appliedPromotions) { this.appliedPromotions = appliedPromotions; }
    public Money getSubtotal() { return subtotal; }
    public void setSubtotal(Money subtotal) { this.subtotal = subtotal; }
    public Money getTotalDiscount() { return totalDiscount; }
    public void setTotalDiscount(Money totalDiscount) { this.totalDiscount = totalDiscount; }
    public Money getTaxAmount() { return taxAmount; }
    public void setTaxAmount(Money taxAmount) { this.taxAmount = taxAmount; }
    public Money getShippingCost() { return shippingCost; }
    public void setShippingCost(Money shippingCost) { this.shippingCost = shippingCost; }

    // ── Pipeline control ──

    public boolean shouldSkip(String stepName) {
        return Boolean.TRUE.equals(skipSteps.get(stepName));
    }
    public void setSkipStep(String stepName, boolean skip) { skipSteps.put(stepName, skip); }
    public Map<String, Boolean> getSkipSteps() { return skipSteps; }
    public void setSkipSteps(Map<String, Boolean> skipSteps) { this.skipSteps = skipSteps; }

    // ── Output getters/setters ──

    public PriceBreakdown getPriceBreakdown() { return priceBreakdown; }
    public void setPriceBreakdown(PriceBreakdown priceBreakdown) { this.priceBreakdown = priceBreakdown; }
    public boolean isError() { return error; }
    public void setError(boolean error) { this.error = error; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
