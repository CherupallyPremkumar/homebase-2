package com.homebase.ecom.dto;

import java.io.Serializable;

public class VariantDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String productId;
    private String sku;
    private String productName;
    private String productState;
    private String attributeKey;
    private String attributeValue;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getProductState() { return productState; }
    public void setProductState(String productState) { this.productState = productState; }
    public String getAttributeKey() { return attributeKey; }
    public void setAttributeKey(String attributeKey) { this.attributeKey = attributeKey; }
    public String getAttributeValue() { return attributeValue; }
    public void setAttributeValue(String attributeValue) { this.attributeValue = attributeValue; }
}
