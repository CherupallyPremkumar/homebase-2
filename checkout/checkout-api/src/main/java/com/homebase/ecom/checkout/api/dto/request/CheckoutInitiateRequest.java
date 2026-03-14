package com.homebase.ecom.checkout.api.dto.request;

import com.homebase.ecom.checkout.api.dto.AddressDTO;
import com.homebase.ecom.checkout.api.dto.MoneyDTO;
import com.homebase.ecom.checkout.api.dto.PaymentMethodDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.Map;

/**
 * Request DTO for initiating checkout
 */
public class CheckoutInitiateRequest {

    @NotNull(message = "Cart ID is required")
    private String cartId;

    @NotNull(message = "Payment method is required")
    @Valid
    private PaymentMethodDTO paymentMethod;

    @NotNull(message = "Shipping address is required")
    @Valid
    private AddressDTO shippingAddress;

    @Valid
    private AddressDTO billingAddress;

    private Boolean sameAsShipping;

    @NotBlank(message = "Shipping method is required")
    private String shippingMethod;

    private String couponCode;

    private String priceCalculationId;

    @NotBlank(message = "Price hash is required")
    private String priceHash;

    @NotNull(message = "Expected total is required")
    @Valid
    private MoneyDTO expectedTotal;

    private Map<String, String> metadata;

    // Getters and Setters
    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public PaymentMethodDTO getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethodDTO paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public AddressDTO getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(AddressDTO shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public AddressDTO getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(AddressDTO billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Boolean getSameAsShipping() {
        return sameAsShipping;
    }

    public void setSameAsShipping(Boolean sameAsShipping) {
        this.sameAsShipping = sameAsShipping;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getPriceCalculationId() {
        return priceCalculationId;
    }

    public void setPriceCalculationId(String priceCalculationId) {
        this.priceCalculationId = priceCalculationId;
    }

    public String getPriceHash() {
        return priceHash;
    }

    public void setPriceHash(String priceHash) {
        this.priceHash = priceHash;
    }

    public MoneyDTO getExpectedTotal() {
        return expectedTotal;
    }

    public void setExpectedTotal(MoneyDTO expectedTotal) {
        this.expectedTotal = expectedTotal;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
