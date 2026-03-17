package com.homebase.ecom.pricing.api.dto;

import com.homebase.ecom.shared.Money;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Response from price verification.
 * If valid, includes the locked price breakdown for checkout to use.
 */
public class PriceVerificationResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean valid;
    private String reason;

    // Locked price data (populated when valid=true)
    private Money subtotal;
    private Money totalDiscount;
    private Money taxAmount;
    private Money shippingCost;
    private Money finalTotal;
    private String breakdownHash;
    private LocalDateTime lockedUntil;

    public PriceVerificationResponseDTO() {}

    public static PriceVerificationResponseDTO invalid(String reason) {
        PriceVerificationResponseDTO dto = new PriceVerificationResponseDTO();
        dto.setValid(false);
        dto.setReason(reason);
        return dto;
    }

    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
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
    public String getBreakdownHash() { return breakdownHash; }
    public void setBreakdownHash(String breakdownHash) { this.breakdownHash = breakdownHash; }
    public LocalDateTime getLockedUntil() { return lockedUntil; }
    public void setLockedUntil(LocalDateTime lockedUntil) { this.lockedUntil = lockedUntil; }
}
