package com.homebase.ecom.catalog.dto;

import java.io.Serializable;

public class CategoryAttributeRow implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String categoryId;
    private String categoryName;
    private String attributeName;
    private String attributeType;
    private boolean isRequired;
    private String allowedValues;
    private int sortOrder;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getAttributeName() { return attributeName; }
    public void setAttributeName(String attributeName) { this.attributeName = attributeName; }
    public String getAttributeType() { return attributeType; }
    public void setAttributeType(String attributeType) { this.attributeType = attributeType; }
    public boolean isIsRequired() { return isRequired; }
    public void setIsRequired(boolean isRequired) { this.isRequired = isRequired; }
    public String getAllowedValues() { return allowedValues; }
    public void setAllowedValues(String allowedValues) { this.allowedValues = allowedValues; }
    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
}
