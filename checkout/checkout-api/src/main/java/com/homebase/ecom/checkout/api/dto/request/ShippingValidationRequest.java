package com.homebase.ecom.checkout.api.dto.request;

import com.homebase.ecom.checkout.api.dto.AddressDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for shipping address validation
 */
public class ShippingValidationRequest {

    @NotNull(message = "Checkout ID is required")
    private String checkoutId;

    @NotNull(message = "Shipping address is required")
    @Valid
    private AddressDTO shippingAddress;

    // Getters and Setters
    public String getCheckoutId() {
        return checkoutId;
    }

    public void setCheckoutId(String checkoutId) {
        this.checkoutId = checkoutId;
    }

    public AddressDTO getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(AddressDTO shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}
