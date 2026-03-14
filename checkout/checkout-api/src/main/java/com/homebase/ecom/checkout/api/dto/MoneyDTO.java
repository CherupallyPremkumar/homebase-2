package com.homebase.ecom.checkout.api.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Money DTO
 */
public class MoneyDTO {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be positive")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a 3-letter ISO code")
    private String currency;

    public MoneyDTO() {
    }

    public MoneyDTO(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    // Getters and Setters
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
