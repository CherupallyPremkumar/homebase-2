package com.homebase.ecom.pricing.api.dto;

import com.homebase.ecom.shared.Money;
import com.homebase.ecom.pricing.domain.model.PriceBreakdown;
import com.homebase.ecom.pricing.domain.model.LockedPriceBreakdown;
import java.io.Serializable;
import java.util.Objects;

public class PricingResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Money finalTotal;
    private String lockToken;
    private String breakdownHash;
    private boolean error;
    private String message;

    public PricingResponseDTO() {}

    public Money getFinalTotal() { return finalTotal; }
    public void setFinalTotal(Money finalTotal) { this.finalTotal = finalTotal; }
    public String getLockToken() { return lockToken; }
    public void setLockToken(String lockToken) { this.lockToken = lockToken; }
    public String getBreakdownHash() { return breakdownHash; }
    public void setBreakdownHash(String breakdownHash) { this.breakdownHash = breakdownHash; }
    public boolean isError() { return error; }
    public void setError(boolean error) { this.error = error; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public static PricingResponseDTO fromBreakdown(PriceBreakdown breakdown) {
        PricingResponseDTO dto = new PricingResponseDTO();
        dto.setFinalTotal(breakdown.getFinalTotal());
        dto.setBreakdownHash(breakdown.getBreakdownHash());
        return dto;
    }

    public static PricingResponseDTO fromLockedBreakdown(LockedPriceBreakdown locked) {
        PricingResponseDTO dto = fromBreakdown(locked.getPriceBreakdown());
        dto.setLockToken(locked.getLockToken());
        return dto;
    }

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
                Objects.equals(finalTotal, that.finalTotal) &&
                Objects.equals(lockToken, that.lockToken) &&
                Objects.equals(breakdownHash, that.breakdownHash) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(finalTotal, lockToken, breakdownHash, error, message);
    }

    @Override
    public String toString() {
        return "PricingResponseDTO{" +
                "finalTotal=" + finalTotal +
                ", lockToken='" + lockToken + '\'' +
                ", breakdownHash='" + breakdownHash + '\'' +
                ", error=" + error +
                ", message='" + message + '\'' +
                '}';
    }
}
