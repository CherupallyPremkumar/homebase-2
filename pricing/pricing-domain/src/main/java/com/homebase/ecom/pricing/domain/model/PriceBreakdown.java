package com.homebase.ecom.pricing.domain.model;

import com.homebase.ecom.shared.Money;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class PriceBreakdown implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Money subtotal;
    private final List<LineItemDiscount> itemDiscounts;
    private final List<AppliedPromotion> appliedPromos;
    private final Money totalDiscount;
    private final Money taxAmount;
    private final Money shippingCost;
    private final Money finalTotal;
    private final String breakdownHash;
    private final LocalDateTime calculatedAt;
    private final String currency;

    private PriceBreakdown(Builder builder) {
        this.subtotal = builder.subtotal;
        this.itemDiscounts = builder.itemDiscounts;
        this.appliedPromos = builder.appliedPromos;
        this.totalDiscount = builder.totalDiscount;
        this.taxAmount = builder.taxAmount;
        this.shippingCost = builder.shippingCost;
        this.finalTotal = builder.finalTotal;
        this.breakdownHash = builder.breakdownHash;
        this.calculatedAt = builder.calculatedAt;
        this.currency = builder.currency;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Money getSubtotal() { return subtotal; }
    public List<LineItemDiscount> getItemDiscounts() { return itemDiscounts; }
    public List<AppliedPromotion> getAppliedPromos() { return appliedPromos; }
    public Money getTotalDiscount() { return totalDiscount; }
    public Money getTaxAmount() { return taxAmount; }
    public Money getShippingCost() { return shippingCost; }
    public Money getFinalTotal() { return finalTotal; }
    public String getBreakdownHash() { return breakdownHash; }
    public LocalDateTime getCalculatedAt() { return calculatedAt; }
    public String getCurrency() { return currency; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceBreakdown that = (PriceBreakdown) o;
        return Objects.equals(subtotal, that.subtotal) &&
                Objects.equals(itemDiscounts, that.itemDiscounts) &&
                Objects.equals(appliedPromos, that.appliedPromos) &&
                Objects.equals(totalDiscount, that.totalDiscount) &&
                Objects.equals(taxAmount, that.taxAmount) &&
                Objects.equals(shippingCost, that.shippingCost) &&
                Objects.equals(finalTotal, that.finalTotal) &&
                Objects.equals(breakdownHash, that.breakdownHash) &&
                Objects.equals(calculatedAt, that.calculatedAt) &&
                Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subtotal, itemDiscounts, appliedPromos, totalDiscount,
                taxAmount, shippingCost, finalTotal, breakdownHash, calculatedAt, currency);
    }

    @Override
    public String toString() {
        return "PriceBreakdown{" +
                "subtotal=" + subtotal +
                ", itemDiscounts=" + itemDiscounts +
                ", appliedPromos=" + appliedPromos +
                ", totalDiscount=" + totalDiscount +
                ", taxAmount=" + taxAmount +
                ", shippingCost=" + shippingCost +
                ", finalTotal=" + finalTotal +
                ", breakdownHash='" + breakdownHash + '\'' +
                ", calculatedAt=" + calculatedAt +
                ", currency='" + currency + '\'' +
                '}';
    }

    public static class Builder {
        private Money subtotal;
        private List<LineItemDiscount> itemDiscounts;
        private List<AppliedPromotion> appliedPromos;
        private Money totalDiscount;
        private Money taxAmount;
        private Money shippingCost;
        private Money finalTotal;
        private String breakdownHash;
        private LocalDateTime calculatedAt;
        private String currency;

        public Builder subtotal(Money subtotal) { this.subtotal = subtotal; return this; }
        public Builder itemDiscounts(List<LineItemDiscount> itemDiscounts) { this.itemDiscounts = itemDiscounts; return this; }
        public Builder appliedPromos(List<AppliedPromotion> appliedPromos) { this.appliedPromos = appliedPromos; return this; }
        public Builder totalDiscount(Money totalDiscount) { this.totalDiscount = totalDiscount; return this; }
        public Builder taxAmount(Money taxAmount) { this.taxAmount = taxAmount; return this; }
        public Builder shippingCost(Money shippingCost) { this.shippingCost = shippingCost; return this; }
        public Builder finalTotal(Money finalTotal) { this.finalTotal = finalTotal; return this; }
        public Builder breakdownHash(String breakdownHash) { this.breakdownHash = breakdownHash; return this; }
        public Builder calculatedAt(LocalDateTime calculatedAt) { this.calculatedAt = calculatedAt; return this; }
        public Builder currency(String currency) { this.currency = currency; return this; }

        public PriceBreakdown build() {
            return new PriceBreakdown(this);
        }
    }
}
