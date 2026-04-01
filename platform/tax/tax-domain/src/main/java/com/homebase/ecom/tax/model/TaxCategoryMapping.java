package com.homebase.ecom.tax.model;

import org.chenile.utils.entity.model.BaseEntity;

/**
 * Maps product category → HSN code.
 * When a product doesn't have an HSN code, we derive it from its category.
 *
 * Example:
 *   ELECTRONICS → 8471 (Computers)
 *   CLOTHING    → 6109 (T-shirts)
 *   FOOD        → 2106 (Food preparations)
 */
public class TaxCategoryMapping extends BaseEntity {
    private String productCategory;          // ELECTRONICS, CLOTHING, FOOD, etc.
    private String subCategory;              // optional: MOBILE, LAPTOP, TSHIRT
    private String hsnCode;                  // 4-8 digit HSN/SAC code
    private String description;

    // Getters and Setters
    public String getProductCategory() { return productCategory; }
    public void setProductCategory(String productCategory) { this.productCategory = productCategory; }
    public String getSubCategory() { return subCategory; }
    public void setSubCategory(String subCategory) { this.subCategory = subCategory; }
    public String getHsnCode() { return hsnCode; }
    public void setHsnCode(String hsnCode) { this.hsnCode = hsnCode; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
