package com.homebase.ecom.tax.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.chenile.jpautils.entity.BaseJpaEntity;

@Entity
@Table(name = "tax_category_mapping")
public class TaxCategoryMappingEntity extends BaseJpaEntity {

    @Column(name = "category_id", nullable = false)
    private String categoryId;

    @Column(name = "hsn_code")
    private String hsnCode;

    @Column(name = "tax_rate_id", nullable = false)
    private String taxRateId;

    // Getters and Setters

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public String getTaxRateId() {
        return taxRateId;
    }

    public void setTaxRateId(String taxRateId) {
        this.taxRateId = taxRateId;
    }
}
