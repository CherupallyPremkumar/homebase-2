package com.homebase.ecom.dto;

import java.io.Serializable;

public class ProductAttributeDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String productId;
    private String attributeId;
    private String attributeCode;
    private String attributeLabel;
    private String inputType;
    private String optionId;
    private String optionLabel;
    private String rawValue;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getAttributeId() { return attributeId; }
    public void setAttributeId(String attributeId) { this.attributeId = attributeId; }
    public String getAttributeCode() { return attributeCode; }
    public void setAttributeCode(String attributeCode) { this.attributeCode = attributeCode; }
    public String getAttributeLabel() { return attributeLabel; }
    public void setAttributeLabel(String attributeLabel) { this.attributeLabel = attributeLabel; }
    public String getInputType() { return inputType; }
    public void setInputType(String inputType) { this.inputType = inputType; }
    public String getOptionId() { return optionId; }
    public void setOptionId(String optionId) { this.optionId = optionId; }
    public String getOptionLabel() { return optionLabel; }
    public void setOptionLabel(String optionLabel) { this.optionLabel = optionLabel; }
    public String getRawValue() { return rawValue; }
    public void setRawValue(String rawValue) { this.rawValue = rawValue; }
}
