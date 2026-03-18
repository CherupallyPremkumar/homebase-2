package com.homebase.ecom.tax.infrastructure.persistence.mapper;

import com.homebase.ecom.tax.model.OrderTaxLine;
import com.homebase.ecom.tax.model.TaxCategoryMapping;
import com.homebase.ecom.tax.model.TaxRate;
import com.homebase.ecom.tax.infrastructure.persistence.entity.OrderTaxLineEntity;
import com.homebase.ecom.tax.infrastructure.persistence.entity.TaxCategoryMappingEntity;
import com.homebase.ecom.tax.infrastructure.persistence.entity.TaxRateEntity;

import java.util.UUID;

public class TaxMapper {

    // --- TaxRate ---

    public TaxRate toModel(TaxRateEntity entity) {
        if (entity == null) return null;
        TaxRate model = new TaxRate();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setRegionCode(entity.getRegionCode());
        model.setTaxType(entity.getTaxType());
        model.setRate(entity.getRate());
        model.setEffectiveFrom(entity.getEffectiveFrom());
        model.setEffectiveTo(entity.getEffectiveTo());
        model.setActive(entity.isActive());
        model.setTenant(entity.tenant);
        return model;
    }

    public TaxRateEntity toEntity(TaxRate model) {
        if (model == null) return null;
        TaxRateEntity entity = new TaxRateEntity();
        if (model.getId() == null || model.getId().isEmpty()) {
            entity.setId("taxrate_" + UUID.randomUUID().toString());
        } else {
            entity.setId(model.getId());
        }
        entity.setName(model.getName());
        entity.setRegionCode(model.getRegionCode());
        entity.setTaxType(model.getTaxType());
        entity.setRate(model.getRate());
        entity.setEffectiveFrom(model.getEffectiveFrom());
        entity.setEffectiveTo(model.getEffectiveTo());
        entity.setActive(model.isActive());
        entity.tenant = model.getTenant();
        return entity;
    }

    // --- TaxCategoryMapping ---

    public TaxCategoryMapping toModel(TaxCategoryMappingEntity entity) {
        if (entity == null) return null;
        TaxCategoryMapping model = new TaxCategoryMapping();
        model.setId(entity.getId());
        model.setCategoryId(entity.getCategoryId());
        model.setHsnCode(entity.getHsnCode());
        model.setTaxRateId(entity.getTaxRateId());
        model.setTenant(entity.tenant);
        return model;
    }

    public TaxCategoryMappingEntity toEntity(TaxCategoryMapping model) {
        if (model == null) return null;
        TaxCategoryMappingEntity entity = new TaxCategoryMappingEntity();
        if (model.getId() == null || model.getId().isEmpty()) {
            entity.setId("taxcatmap_" + UUID.randomUUID().toString());
        } else {
            entity.setId(model.getId());
        }
        entity.setCategoryId(model.getCategoryId());
        entity.setHsnCode(model.getHsnCode());
        entity.setTaxRateId(model.getTaxRateId());
        entity.tenant = model.getTenant();
        return entity;
    }

    // --- OrderTaxLine ---

    public OrderTaxLine toModel(OrderTaxLineEntity entity) {
        if (entity == null) return null;
        OrderTaxLine model = new OrderTaxLine();
        model.setId(entity.getId());
        model.setOrderId(entity.getOrderId());
        model.setOrderItemId(entity.getOrderItemId());
        model.setTaxRateId(entity.getTaxRateId());
        model.setTaxType(entity.getTaxType());
        model.setTaxableAmount(entity.getTaxableAmount());
        model.setTaxAmount(entity.getTaxAmount());
        model.setRateApplied(entity.getRateApplied());
        model.setTenant(entity.tenant);
        return model;
    }

    public OrderTaxLineEntity toEntity(OrderTaxLine model) {
        if (model == null) return null;
        OrderTaxLineEntity entity = new OrderTaxLineEntity();
        if (model.getId() == null || model.getId().isEmpty()) {
            entity.setId("ordtaxline_" + UUID.randomUUID().toString());
        } else {
            entity.setId(model.getId());
        }
        entity.setOrderId(model.getOrderId());
        entity.setOrderItemId(model.getOrderItemId());
        entity.setTaxRateId(model.getTaxRateId());
        entity.setTaxType(model.getTaxType());
        entity.setTaxableAmount(model.getTaxableAmount());
        entity.setTaxAmount(model.getTaxAmount());
        entity.setRateApplied(model.getRateApplied());
        entity.tenant = model.getTenant();
        return entity;
    }
}
