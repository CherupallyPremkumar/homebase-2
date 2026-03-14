package com.homebase.ecom.checkout.api.dto.request;

import com.homebase.ecom.checkout.api.dto.AddressDTO;
import com.homebase.ecom.checkout.api.dto.ShippingItemDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

/**
 * Request DTO for shipping estimate
 */
public class ShippingEstimateRequest {

    @NotNull(message = "Cart ID is required")
    private String cartId;

    @NotNull(message = "Destination address is required")
    @Valid
    private AddressDTO destinationAddress;

    @NotEmpty(message = "Items list cannot be empty")
    @Valid
    private List<ShippingItemDTO> items;

    // Getters and Setters
    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public AddressDTO getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(AddressDTO destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public List<ShippingItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ShippingItemDTO> items) {
        this.items = items;
    }
}
