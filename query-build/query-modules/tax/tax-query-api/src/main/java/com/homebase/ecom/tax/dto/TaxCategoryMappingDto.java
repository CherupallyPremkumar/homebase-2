package com.homebase.ecom.tax.dto;

import java.io.Serializable;

public class TaxCategoryMappingDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String productCategory;
    private String subCategory;
    private String hsnCode;
    private String description;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProductCategory() { return productCategory; }
    public void setProductCategory(String productCategory) { this.productCategory = productCategory; }
    public String getSubCategory() { return subCategory; }
    public void setSubCategory(String subCategory) { this.subCategory = subCategory; }
    public String getHsnCode() { return hsnCode; }
    public void setHsnCode(String hsnCode) { this.hsnCode = hsnCode; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
