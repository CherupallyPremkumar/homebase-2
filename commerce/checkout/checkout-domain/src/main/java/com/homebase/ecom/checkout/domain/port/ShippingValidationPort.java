package com.homebase.ecom.checkout.domain.port;

/**
 * Port for validating shipping during checkout.
 * Adapter calls Shipping service to validate address and calculate cost.
 */
public interface ShippingValidationPort {

    /**
     * Validates shipping address and returns shipping cost.
     * @return shipping cost in minor units (paise)
     */
    ShippingResult validate(String shippingAddressId, String shippingMethod, String currency);

    record ShippingResult(
        boolean valid,
        long shippingCost,
        String estimatedDelivery,
        String reason
    ) {}
}
