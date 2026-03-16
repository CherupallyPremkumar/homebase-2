package com.homebase.ecom.pricing.api.dto;

import com.homebase.ecom.pricing.api.dto.CartSnapshotDTO;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class PricingRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String couponCode;
    private List<String> couponCodes;
    private CartSnapshotDTO cart;

    public PricingRequestDTO() {}

    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }
    public List<String> getCouponCodes() { return couponCodes; }
    public void setCouponCodes(List<String> couponCodes) { this.couponCodes = couponCodes; }
    public CartSnapshotDTO getCart() { return cart; }
    public void setCart(CartSnapshotDTO cart) { this.cart = cart; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PricingRequestDTO that = (PricingRequestDTO) o;
        return Objects.equals(couponCode, that.couponCode) &&
                Objects.equals(cart, that.cart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(couponCode, cart);
    }

    @Override
    public String toString() {
        return "PricingRequestDTO{" +
                "couponCode='" + couponCode + '\'' +
                ", cart=" + cart +
                '}';
    }
}
