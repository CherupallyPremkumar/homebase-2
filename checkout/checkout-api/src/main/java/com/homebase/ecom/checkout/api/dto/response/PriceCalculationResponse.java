package com.homebase.ecom.checkout.api.dto.response;

import com.homebase.ecom.checkout.api.dto.CartItemDTO;
import com.homebase.ecom.checkout.api.dto.PriceBreakdownDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for price calculation
 */
public class PriceCalculationResponse {

    private String calculationId;
    private LocalDateTime timestamp;
    private String currency;

    private PriceBreakdownDTO breakdown;
    private List<CartItemDTO> items;

    private LocalDateTime expiresAt;

    // Getters and Setters
    public String getCalculationId() {
        return calculationId;
    }

    public void setCalculationId(String calculationId) {
        this.calculationId = calculationId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public PriceBreakdownDTO getBreakdown() {
        return breakdown;
    }

    public void setBreakdown(PriceBreakdownDTO breakdown) {
        this.breakdown = breakdown;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
