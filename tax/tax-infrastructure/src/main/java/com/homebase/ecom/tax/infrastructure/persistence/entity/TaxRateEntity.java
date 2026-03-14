package com.homebase.ecom.tax.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.chenile.jpautils.entity.BaseJpaEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tax_rates")
public class TaxRateEntity extends BaseJpaEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "region_code", nullable = false)
    private String regionCode;

    @Column(name = "tax_type", nullable = false)
    private String taxType;

    @Column(name = "rate", nullable = false, precision = 10, scale = 4)
    private BigDecimal rate;

    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(name = "active", nullable = false)
    private boolean active;

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public LocalDate getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(LocalDate effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
