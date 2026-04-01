package com.homebase.ecom.tax.infrastructure.persistence.mapper;

import com.homebase.ecom.tax.model.*;
import com.homebase.ecom.tax.infrastructure.persistence.entity.*;

public class TaxMapper {

    // --- TaxRate ---

    public TaxRate toModel(TaxRateEntity e) {
        if (e == null) return null;
        TaxRate m = new TaxRate();
        m.setId(e.getId());
        m.setHsnCode(e.getHsnCode());
        m.setDescription(e.getDescription());
        m.setGstRate(e.getGstRate());
        m.setCessRate(e.getCessRate());
        m.setTcsRate(e.getTcsRate());
        m.setEffectiveFrom(e.getEffectiveFrom());
        m.setEffectiveTo(e.getEffectiveTo());
        m.setActive(e.isActive());
        return m;
    }

    public TaxRateEntity toEntity(TaxRate m) {
        if (m == null) return null;
        TaxRateEntity e = new TaxRateEntity();
        e.setId(m.getId());
        e.setHsnCode(m.getHsnCode());
        e.setDescription(m.getDescription());
        e.setGstRate(m.getGstRate());
        e.setCessRate(m.getCessRate());
        e.setTcsRate(m.getTcsRate());
        e.setEffectiveFrom(m.getEffectiveFrom());
        e.setEffectiveTo(m.getEffectiveTo());
        e.setActive(m.isActive());
        return e;
    }

    // --- TaxCategoryMapping ---

    public TaxCategoryMapping toModel(TaxCategoryMappingEntity e) {
        if (e == null) return null;
        TaxCategoryMapping m = new TaxCategoryMapping();
        m.setId(e.getId());
        m.setProductCategory(e.getProductCategory());
        m.setSubCategory(e.getSubCategory());
        m.setHsnCode(e.getHsnCode());
        m.setDescription(e.getDescription());
        return m;
    }

    public TaxCategoryMappingEntity toEntity(TaxCategoryMapping m) {
        if (m == null) return null;
        TaxCategoryMappingEntity e = new TaxCategoryMappingEntity();
        e.setId(m.getId());
        e.setProductCategory(m.getProductCategory());
        e.setSubCategory(m.getSubCategory());
        e.setHsnCode(m.getHsnCode());
        e.setDescription(m.getDescription());
        return e;
    }

    // --- TaxExemption ---

    public TaxExemption toModel(TaxExemptionEntity e) {
        if (e == null) return null;
        TaxExemption m = new TaxExemption();
        m.setId(e.getId());
        m.setExemptionType(e.getExemptionType());
        m.setValue(e.getValue());
        m.setDescription(e.getDescription());
        m.setFullExemption(e.isFullExemption());
        m.setActive(e.isActive());
        return m;
    }

    public TaxExemptionEntity toEntity(TaxExemption m) {
        if (m == null) return null;
        TaxExemptionEntity e = new TaxExemptionEntity();
        e.setId(m.getId());
        e.setExemptionType(m.getExemptionType());
        e.setValue(m.getValue());
        e.setDescription(m.getDescription());
        e.setFullExemption(m.isFullExemption());
        e.setActive(m.isActive());
        return e;
    }

    // --- TaxRegion ---

    public TaxRegion toModel(TaxRegionEntity e) {
        if (e == null) return null;
        TaxRegion m = new TaxRegion();
        m.setId(e.getId());
        m.setStateCode(e.getStateCode());
        m.setStateName(e.getStateName());
        m.setStateAlpha(e.getStateAlpha());
        m.setUnionTerritory(e.isUnionTerritory());
        m.setActive(e.isActive());
        return m;
    }

    public TaxRegionEntity toEntity(TaxRegion m) {
        if (m == null) return null;
        TaxRegionEntity e = new TaxRegionEntity();
        e.setId(m.getId());
        e.setStateCode(m.getStateCode());
        e.setStateName(m.getStateName());
        e.setStateAlpha(m.getStateAlpha());
        e.setUnionTerritory(m.isUnionTerritory());
        e.setActive(m.isActive());
        return e;
    }

    // --- OrderTaxLine ---

    public OrderTaxLine toModel(OrderTaxLineEntity e) {
        if (e == null) return null;
        OrderTaxLine m = new OrderTaxLine();
        m.setId(e.getId());
        m.setOrderId(e.getOrderId());
        m.setVariantId(e.getVariantId());
        m.setHsnCode(e.getHsnCode());
        m.setTaxType(e.getTaxType());
        m.setTaxableAmount(e.getTaxableAmount());
        m.setGstRate(e.getGstRate());
        m.setCgstAmount(e.getCgstAmount());
        m.setSgstAmount(e.getSgstAmount());
        m.setIgstAmount(e.getIgstAmount());
        m.setCessAmount(e.getCessAmount());
        m.setTcsAmount(e.getTcsAmount());
        m.setTotalTax(e.getTotalTax());
        return m;
    }

    public OrderTaxLineEntity toEntity(OrderTaxLine m) {
        if (m == null) return null;
        OrderTaxLineEntity e = new OrderTaxLineEntity();
        e.setId(m.getId());
        e.setOrderId(m.getOrderId());
        e.setVariantId(m.getVariantId());
        e.setHsnCode(m.getHsnCode());
        e.setTaxType(m.getTaxType());
        e.setTaxableAmount(m.getTaxableAmount());
        e.setGstRate(m.getGstRate());
        e.setCgstAmount(m.getCgstAmount());
        e.setSgstAmount(m.getSgstAmount());
        e.setIgstAmount(m.getIgstAmount());
        e.setCessAmount(m.getCessAmount());
        e.setTcsAmount(m.getTcsAmount());
        e.setTotalTax(m.getTotalTax());
        return e;
    }
}
