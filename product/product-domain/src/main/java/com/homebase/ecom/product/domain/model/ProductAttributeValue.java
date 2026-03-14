package com.homebase.ecom.product.domain.model;

import org.chenile.utils.entity.model.BaseEntity;

public class ProductAttributeValue extends BaseEntity {

    private String productId;
    private String attributeId;
    private String optionId; // for SELECT/MULTISELECT
    private String rawValue; // for TEXT/NUMBER/DATE



    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getAttributeId() { return attributeId; }
    public void setAttributeId(String attributeId) { this.attributeId = attributeId; }

    public String getOptionId() { return optionId; }
    public void setOptionId(String optionId) { this.optionId = optionId; }

    public String getRawValue() { return rawValue; }
    public void setRawValue(String rawValue) { this.rawValue = rawValue; }
}
