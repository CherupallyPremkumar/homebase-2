package com.homebase.ecom.dto;

import com.homebase.ecom.shared.Money;

import java.io.Serializable;
import java.math.BigDecimal;

public class OfferDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String productId;
    private String sellerId;
    private Money price;
    private Boolean active;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }
}
