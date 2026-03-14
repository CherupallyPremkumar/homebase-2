package com.homebase.ecom.promo.dto;

import java.util.List;

public class PromoCartDTO {
    private String cartId;
    private List<PromoCartItemDTO> items;
    private Double totalAmount;

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public List<PromoCartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<PromoCartItemDTO> items) {
        this.items = items;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
