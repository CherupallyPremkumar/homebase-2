package com.homebase.ecom.pricing.domain.model;

import com.homebase.ecom.shared.Money;
import java.util.ArrayList;
import java.util.List;

/**
 * Per-line-item pricing enriched by each pipeline command.
 * Starts with base price, accumulates discounts, ends with lineTotal.
 */
public class LineItemPricing {

    private String variantId;
    private String productId;
    private String sellerId;
    private int quantity;
    private Money unitPrice;
    private Money currentPrice; // running price after each discount step
    private Money lineSubtotal; // unitPrice * quantity (before discounts)
    private Money lineDiscount; // total discount for this line
    private Money lineTotal;    // final line total after all discounts
    private List<DiscountResult> appliedDiscounts = new ArrayList<>();

    public LineItemPricing() {}

    public String getVariantId() { return variantId; }
    public void setVariantId(String variantId) { this.variantId = variantId; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public Money getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Money unitPrice) { this.unitPrice = unitPrice; }
    public Money getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(Money currentPrice) { this.currentPrice = currentPrice; }
    public Money getLineSubtotal() { return lineSubtotal; }
    public void setLineSubtotal(Money lineSubtotal) { this.lineSubtotal = lineSubtotal; }
    public Money getLineDiscount() { return lineDiscount; }
    public void setLineDiscount(Money lineDiscount) { this.lineDiscount = lineDiscount; }
    public Money getLineTotal() { return lineTotal; }
    public void setLineTotal(Money lineTotal) { this.lineTotal = lineTotal; }
    public List<DiscountResult> getAppliedDiscounts() { return appliedDiscounts; }
    public void setAppliedDiscounts(List<DiscountResult> appliedDiscounts) { this.appliedDiscounts = appliedDiscounts; }

    public void addDiscount(DiscountResult discount) {
        this.appliedDiscounts.add(discount);
    }
}
