package com.homebase.ecom.pricing.infrastructure.client.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class ValidateCouponRequest {
    private String code;
    private String userId;
    private BigDecimal cartTotal;
    private String region;
    private String customerSegment;

    public ValidateCouponRequest() {}

    public ValidateCouponRequest(String code, String userId, BigDecimal cartTotal, String region, String customerSegment) {
        this.code = code;
        this.userId = userId;
        this.cartTotal = cartTotal;
        this.region = region;
        this.customerSegment = customerSegment;
    }

    private ValidateCouponRequest(Builder builder) {
        this.code = builder.code;
        this.userId = builder.userId;
        this.cartTotal = builder.cartTotal;
        this.region = builder.region;
        this.customerSegment = builder.customerSegment;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public BigDecimal getCartTotal() { return cartTotal; }
    public void setCartTotal(BigDecimal cartTotal) { this.cartTotal = cartTotal; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getCustomerSegment() { return customerSegment; }
    public void setCustomerSegment(String customerSegment) { this.customerSegment = customerSegment; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidateCouponRequest that = (ValidateCouponRequest) o;
        return Objects.equals(code, that.code) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(cartTotal, that.cartTotal) &&
                Objects.equals(region, that.region) &&
                Objects.equals(customerSegment, that.customerSegment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, userId, cartTotal, region, customerSegment);
    }

    @Override
    public String toString() {
        return "ValidateCouponRequest{" +
                "code='" + code + '\'' +
                ", userId='" + userId + '\'' +
                ", cartTotal=" + cartTotal +
                ", region='" + region + '\'' +
                ", customerSegment='" + customerSegment + '\'' +
                '}';
    }

    public static class Builder {
        private String code;
        private String userId;
        private BigDecimal cartTotal;
        private String region;
        private String customerSegment;

        public Builder code(String code) { this.code = code; return this; }
        public Builder userId(String userId) { this.userId = userId; return this; }
        public Builder cartTotal(BigDecimal cartTotal) { this.cartTotal = cartTotal; return this; }
        public Builder region(String region) { this.region = region; return this; }
        public Builder customerSegment(String customerSegment) { this.customerSegment = customerSegment; return this; }

        public ValidateCouponRequest build() {
            return new ValidateCouponRequest(this);
        }
    }
}
