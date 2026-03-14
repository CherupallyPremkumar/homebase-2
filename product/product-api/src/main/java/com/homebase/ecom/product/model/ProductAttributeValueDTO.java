package com.homebase.ecom.product.model;

import java.io.Serializable;

public class ProductAttributeValueDTO implements Serializable {
    private String id;
    private String productId;
    private String attributeId;
    private String optionId;
    private String rawValue;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getAttributeId() { return attributeId; }
    public void setAttributeId(String attributeId) { this.attributeId = attributeId; }

    public String getOptionId() { return optionId; }
    public void setOptionId(String optionId) { this.optionId = optionId; }

    public String getRawValue() { return rawValue; }
    public void setRawValue(String rawValue) { this.rawValue = rawValue; }
}
