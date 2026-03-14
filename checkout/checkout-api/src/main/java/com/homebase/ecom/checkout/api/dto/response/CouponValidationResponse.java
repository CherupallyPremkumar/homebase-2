package com.homebase.ecom.checkout.api.dto.response;

import com.homebase.ecom.checkout.api.dto.CouponRestrictionsDTO;
import com.homebase.ecom.checkout.api.dto.MoneyDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for coupon validation
 */
public class CouponValidationResponse {

    private boolean valid;
    private String couponCode;

    private String discountType;
    private Double discountValue;
    private MoneyDTO discountAmount;

    private MoneyDTO minimumPurchase;
    private LocalDateTime expiresAt;
    private Integer usageRemaining;

    private CouponRestrictionsDTO restrictions;
    private String message;

    private List<String> errors;

    // Getters and Setters
    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public Double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
    }

    public MoneyDTO getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(MoneyDTO discountAmount) {
        this.discountAmount = discountAmount;
    }

    public MoneyDTO getMinimumPurchase() {
        return minimumPurchase;
    }

    public void setMinimumPurchase(MoneyDTO minimumPurchase) {
        this.minimumPurchase = minimumPurchase;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Integer getUsageRemaining() {
        return usageRemaining;
    }

    public void setUsageRemaining(Integer usageRemaining) {
        this.usageRemaining = usageRemaining;
    }

    public CouponRestrictionsDTO getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(CouponRestrictionsDTO restrictions) {
        this.restrictions = restrictions;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
