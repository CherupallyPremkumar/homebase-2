package com.homebase.ecom.uiquery.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductVariantWithPriceQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String variantId;
    private String productId;
    private String sku;
    private BigDecimal sellingPrice;
    private BigDecimal mrp;
    private int availableQuantity;
    private boolean inStock;
    private String offerId;
    private String offerState;

    public String getVariantId() { return variantId; }
    public void setVariantId(String variantId) { this.variantId = variantId; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public BigDecimal getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; }
    public BigDecimal getMrp() { return mrp; }
    public void setMrp(BigDecimal mrp) { this.mrp = mrp; }
    public int getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(int availableQuantity) { this.availableQuantity = availableQuantity; }
    public boolean getInStock() { return inStock; }
    public void setInStock(boolean inStock) { this.inStock = inStock; }
    public String getOfferId() { return offerId; }
    public void setOfferId(String offerId) { this.offerId = offerId; }
    public String getOfferState() { return offerState; }
    public void setOfferState(String offerState) { this.offerState = offerState; }
}
