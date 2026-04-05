package com.homebase.ecom.uiquery.dto;

import java.io.Serializable;

public class OutOfStockQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String productId;
    private String productName;
    private String variantId;
    private String sku;

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getVariantId() { return variantId; }
    public void setVariantId(String variantId) { this.variantId = variantId; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
}
