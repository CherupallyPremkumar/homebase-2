package com.homebase.ecom.checkout.api.dto.request;

import com.homebase.ecom.checkout.api.dto.MoneyDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * Request DTO for coupon validation
 */
public class CouponValidationRequest {

    @NotBlank(message = "Coupon code is required")
    @Size(min = 3, max = 50, message = "Coupon code must be between 3 and 50 characters")
    private String couponCode;

    @NotNull(message = "User ID is required")
    private String userId;

    @NotNull(message = "Cart ID is required")
    private String cartId;

    @NotNull(message = "Cart total is required")
    @Valid
    private MoneyDTO cartTotal;

    // Getters and Setters
    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode != null ? couponCode.toUpperCase() : null;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public MoneyDTO getCartTotal() {
        return cartTotal;
    }

    public void setCartTotal(MoneyDTO cartTotal) {
        this.cartTotal = cartTotal;
    }
}
