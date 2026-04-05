package com.homebase.ecom.catalog.port.client;

import java.math.BigDecimal;

/**
 * ACL value object — catalog's own view of an offer.
 */
public class OfferSnapshot {
    private String id;
    private String productId;
    private String sellerId;
    private BigDecimal offerPrice;
    private String status;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
    public BigDecimal getOfferPrice() { return offerPrice; }
    public void setOfferPrice(BigDecimal offerPrice) { this.offerPrice = offerPrice; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
