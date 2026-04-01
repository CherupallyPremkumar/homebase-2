package com.homebase.ecom.analytics.infrastructure.persistence.mapper;

import com.homebase.ecom.analytics.model.DailySalesSummary;
import com.homebase.ecom.analytics.model.ProductPerformance;
import com.homebase.ecom.analytics.model.SupplierPerformance;
import com.homebase.ecom.analytics.infrastructure.persistence.entity.DailySalesSummaryEntity;
import com.homebase.ecom.analytics.infrastructure.persistence.entity.ProductPerformanceEntity;
import com.homebase.ecom.analytics.infrastructure.persistence.entity.SupplierPerformanceEntity;

public class AnalyticsMapper {

    // --- DailySalesSummary ---

    public DailySalesSummary toModel(DailySalesSummaryEntity entity) {
        if (entity == null) return null;
        DailySalesSummary model = new DailySalesSummary();
        model.setId(entity.getId());
        model.setSummaryDate(entity.getSummaryDate());
        model.setTotalOrders(entity.getTotalOrders());
        model.setTotalRevenue(entity.getTotalRevenue());
        model.setTotalUnitsSold(entity.getTotalUnitsSold());
        model.setTotalDiscount(entity.getTotalDiscount());
        model.setTotalTax(entity.getTotalTax());
        model.setTotalShipping(entity.getTotalShipping());
        model.setAvgOrderValue(entity.getAvgOrderValue());
        model.setNewCustomers(entity.getNewCustomers());
        model.setReturningCustomers(entity.getReturningCustomers());
        model.setCurrency(entity.getCurrency());
        model.setTenant(entity.tenant);
        return model;
    }

    public DailySalesSummaryEntity toEntity(DailySalesSummary model) {
        if (model == null) return null;
        DailySalesSummaryEntity entity = new DailySalesSummaryEntity();
        entity.setId(model.getId());
        entity.setSummaryDate(model.getSummaryDate());
        entity.setTotalOrders(model.getTotalOrders());
        entity.setTotalRevenue(model.getTotalRevenue());
        entity.setTotalUnitsSold(model.getTotalUnitsSold());
        entity.setTotalDiscount(model.getTotalDiscount());
        entity.setTotalTax(model.getTotalTax());
        entity.setTotalShipping(model.getTotalShipping());
        entity.setAvgOrderValue(model.getAvgOrderValue());
        entity.setNewCustomers(model.getNewCustomers());
        entity.setReturningCustomers(model.getReturningCustomers());
        entity.setCurrency(model.getCurrency());
        entity.tenant = model.getTenant();
        return entity;
    }

    // --- ProductPerformance ---

    public ProductPerformance toModel(ProductPerformanceEntity entity) {
        if (entity == null) return null;
        ProductPerformance model = new ProductPerformance();
        model.setId(entity.getId());
        model.setProductId(entity.getProductId());
        model.setPeriodDate(entity.getPeriodDate());
        model.setViews(entity.getViews());
        model.setAddToCartCount(entity.getAddToCartCount());
        model.setPurchases(entity.getPurchases());
        model.setUnitsSold(entity.getUnitsSold());
        model.setRevenue(entity.getRevenue());
        model.setReturnsCount(entity.getReturnsCount());
        model.setAvgRating(entity.getAvgRating());
        model.setTenant(entity.tenant);
        return model;
    }

    public ProductPerformanceEntity toEntity(ProductPerformance model) {
        if (model == null) return null;
        ProductPerformanceEntity entity = new ProductPerformanceEntity();
        entity.setId(model.getId());
        entity.setProductId(model.getProductId());
        entity.setPeriodDate(model.getPeriodDate());
        entity.setViews(model.getViews());
        entity.setAddToCartCount(model.getAddToCartCount());
        entity.setPurchases(model.getPurchases());
        entity.setUnitsSold(model.getUnitsSold());
        entity.setRevenue(model.getRevenue());
        entity.setReturnsCount(model.getReturnsCount());
        entity.setAvgRating(model.getAvgRating());
        entity.tenant = model.getTenant();
        return entity;
    }

    // --- SupplierPerformance ---

    public SupplierPerformance toModel(SupplierPerformanceEntity entity) {
        if (entity == null) return null;
        SupplierPerformance model = new SupplierPerformance();
        model.setId(entity.getId());
        model.setSupplierId(entity.getSupplierId());
        model.setPeriodMonth(entity.getPeriodMonth());
        model.setPeriodYear(entity.getPeriodYear());
        model.setTotalOrders(entity.getTotalOrders());
        model.setTotalRevenue(entity.getTotalRevenue());
        model.setTotalReturns(entity.getTotalReturns());
        model.setAvgFulfillmentDays(entity.getAvgFulfillmentDays());
        model.setAvgRating(entity.getAvgRating());
        model.setCancellationRate(entity.getCancellationRate());
        model.setTenant(entity.tenant);
        return model;
    }

    public SupplierPerformanceEntity toEntity(SupplierPerformance model) {
        if (model == null) return null;
        SupplierPerformanceEntity entity = new SupplierPerformanceEntity();
        entity.setId(model.getId());
        entity.setSupplierId(model.getSupplierId());
        entity.setPeriodMonth(model.getPeriodMonth());
        entity.setPeriodYear(model.getPeriodYear());
        entity.setTotalOrders(model.getTotalOrders());
        entity.setTotalRevenue(model.getTotalRevenue());
        entity.setTotalReturns(model.getTotalReturns());
        entity.setAvgFulfillmentDays(model.getAvgFulfillmentDays());
        entity.setAvgRating(model.getAvgRating());
        entity.setCancellationRate(model.getCancellationRate());
        entity.tenant = model.getTenant();
        return entity;
    }
}
