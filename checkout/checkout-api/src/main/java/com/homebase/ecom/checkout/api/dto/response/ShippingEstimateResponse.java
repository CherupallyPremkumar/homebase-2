package com.homebase.ecom.checkout.api.dto.response;

import com.homebase.ecom.checkout.api.dto.ShippingOptionDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for shipping estimate
 */
public class ShippingEstimateResponse {

    private String estimateId;
    private List<ShippingOptionDTO> shippingOptions;
    private String defaultMethod;
    private LocalDateTime validUntil;

    // Getters and Setters
    public String getEstimateId() {
        return estimateId;
    }

    public void setEstimateId(String estimateId) {
        this.estimateId = estimateId;
    }

    public List<ShippingOptionDTO> getShippingOptions() {
        return shippingOptions;
    }

    public void setShippingOptions(List<ShippingOptionDTO> shippingOptions) {
        this.shippingOptions = shippingOptions;
    }

    public String getDefaultMethod() {
        return defaultMethod;
    }

    public void setDefaultMethod(String defaultMethod) {
        this.defaultMethod = defaultMethod;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }
}
