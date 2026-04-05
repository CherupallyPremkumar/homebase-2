package com.homebase.ecom.pricing.api.dto;

import com.homebase.ecom.shared.Money;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Full pricing breakdown returned by the Pricing Service.
 * Cart stores these values directly — it does ZERO math.
 *
 * subtotal = sum of lineItem subtotals
 * totalDiscount = sum of all discounts (line-level + coupon)
 * finalTotal = subtotal - totalDiscount + taxAmount + shippingCost
 */
public class PricingResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // ── Cart-level totals ──
    private Money subtotal;
    private Money totalDiscount;
    private Money taxAmount;
    private Money shippingCost;
    private Money finalTotal;

    // ── Line-level breakdown ──
    private List<LineItemPricingDTO> lineItems;

    // ── Integrity & locking ──
    private String lockToken;
    private String breakdownHash;

    // ── Error handling ──
    private boolean error;
    private String message;

    public PricingResponseDTO() {}

    // ── Getters & Setters ──

    public Money getSubtotal() { return subtotal; }
    public void setSubtotal(Money subtotal) { this.subtotal = subtotal; }
    public Money getTotalDiscount() { return totalDiscount; }
    public void setTotalDiscount(Money totalDiscount) { this.totalDiscount = totalDiscount; }
    public Money getTaxAmount() { return taxAmount; }
    public void setTaxAmount(Money taxAmount) { this.taxAmount = taxAmount; }
    public Money getShippingCost() { return shippingCost; }
    public void setShippingCost(Money shippingCost) { this.shippingCost = shippingCost; }
    public Money getFinalTotal() { return finalTotal; }
    public void setFinalTotal(Money finalTotal) { this.finalTotal = finalTotal; }
    public List<LineItemPricingDTO> getLineItems() { return lineItems; }
    public void setLineItems(List<LineItemPricingDTO> lineItems) { this.lineItems = lineItems; }
    public String getLockToken() { return lockToken; }
    public void setLockToken(String lockToken) { this.lockToken = lockToken; }
    public String getBreakdownHash() { return breakdownHash; }
    public void setBreakdownHash(String breakdownHash) { this.breakdownHash = breakdownHash; }
    public boolean isError() { return error; }
    public void setError(boolean error) { this.error = error; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public static PricingResponseDTO error(String message) {
        PricingResponseDTO dto = new PricingResponseDTO();
        dto.setError(true);
        dto.setMessage(message);
        return dto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PricingResponseDTO that = (PricingResponseDTO) o;
        return error == that.error &&
                Objects.equals(subtotal, that.subtotal) &&
                Objects.equals(totalDiscount, that.totalDiscount) &&
                Objects.equals(finalTotal, that.finalTotal) &&
                Objects.equals(lockToken, that.lockToken) &&
                Objects.equals(breakdownHash, that.breakdownHash) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subtotal, totalDiscount, finalTotal, lockToken, breakdownHash, error, message);
    }

    @Override
    public String toString() {
        return "PricingResponseDTO{" +
                "subtotal=" + subtotal +
                ", totalDiscount=" + totalDiscount +
                ", taxAmount=" + taxAmount +
                ", shippingCost=" + shippingCost +
                ", finalTotal=" + finalTotal +
                ", lineItems=" + (lineItems != null ? lineItems.size() + " items" : "null") +
                ", error=" + error +
                ", message='" + message + '\'' +
                '}';
    }
}
