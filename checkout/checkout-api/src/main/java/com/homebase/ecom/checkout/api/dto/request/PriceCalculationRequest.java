package com.homebase.ecom.checkout.api.dto.request;

import com.homebase.ecom.checkout.api.dto.AddressDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * Request DTO for price calculation
 */
public class PriceCalculationRequest {

    @NotNull(message = "Cart ID is required")
    private String cartId;

    @NotNull(message = "User ID is required")
    private String userId;

    private String couponCode;

    @NotNull(message = "Shipping address is required")
    @Valid
    private AddressDTO shippingAddress;

    @NotBlank(message = "Shipping method is required")
    private String shippingMethod;

    @NotBlank(message = "Currency is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a 3-letter ISO code")
    private String currency;

    // Getters and Setters
    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public AddressDTO getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(AddressDTO shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
