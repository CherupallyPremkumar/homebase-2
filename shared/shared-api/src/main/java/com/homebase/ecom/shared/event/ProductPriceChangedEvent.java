package com.homebase.ecom.shared.event;

import com.homebase.ecom.shared.Money;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Event published when a product's price or seller changes.
 */
public class ProductPriceChangedEvent implements Serializable {
    private String productId;
    private Money newPrice;
    private String sellerId;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ProductPriceChangedEvent() {}

    public ProductPriceChangedEvent(String productId, Money newPrice, String sellerId) {
        this.productId = productId;
        this.newPrice = newPrice;
        this.sellerId = sellerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Money getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(Money newPrice) {
        this.newPrice = newPrice;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
