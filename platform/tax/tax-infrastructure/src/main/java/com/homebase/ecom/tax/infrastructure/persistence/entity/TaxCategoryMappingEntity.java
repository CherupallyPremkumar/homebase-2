package com.homebase.ecom.tax.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.chenile.jpautils.entity.BaseJpaEntity;

@Entity
@Table(name = "tax_category_mapping")
public class TaxCategoryMappingEntity extends BaseJpaEntity {

    @Column(name = "product_category", nullable = false) private String productCategory;
    @Column(name = "sub_category") private String subCategory;
    @Column(name = "hsn_code", nullable = false) private String hsnCode;
    @Column(name = "description") private String description;

    public String getProductCategory() { return productCategory; }
    public void setProductCategory(String productCategory) { this.productCategory = productCategory; }
    public String getSubCategory() { return subCategory; }
    public void setSubCategory(String subCategory) { this.subCategory = subCategory; }
    public String getHsnCode() { return hsnCode; }
    public void setHsnCode(String hsnCode) { this.hsnCode = hsnCode; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
