package com.homebase.ecom.analytics.service.impl;

import com.homebase.ecom.analytics.dto.DailySalesDto;
import com.homebase.ecom.analytics.dto.ProductPerformanceDto;
import com.homebase.ecom.analytics.dto.SupplierPerformanceDto;
import com.homebase.ecom.analytics.model.DailySalesSummary;
import com.homebase.ecom.analytics.model.ProductPerformance;
import com.homebase.ecom.analytics.model.SupplierPerformance;
import com.homebase.ecom.analytics.port.DailySalesSummaryRepository;
import com.homebase.ecom.analytics.port.ProductPerformanceRepository;
import com.homebase.ecom.analytics.port.SupplierPerformanceRepository;
import com.homebase.ecom.analytics.infrastructure.persistence.mapper.AnalyticsMapper;
import com.homebase.ecom.analytics.service.AnalyticsService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class AnalyticsServiceImpl implements AnalyticsService {

    private final DailySalesSummaryRepository dailySalesRepo;
    private final ProductPerformanceRepository productPerfRepo;
    private final SupplierPerformanceRepository supplierPerfRepo;
    private final AnalyticsMapper mapper;

    public AnalyticsServiceImpl(
            DailySalesSummaryRepository dailySalesRepo,
            ProductPerformanceRepository productPerfRepo,
            SupplierPerformanceRepository supplierPerfRepo,
            AnalyticsMapper mapper) {
        this.dailySalesRepo = dailySalesRepo;
        this.productPerfRepo = productPerfRepo;
        this.supplierPerfRepo = supplierPerfRepo;
        this.mapper = mapper;
    }

    @Override
    public List<DailySalesDto> getDailySales(LocalDate from, LocalDate to) {
        return dailySalesRepo.findByDateRange(from, to)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductPerformanceDto> getProductPerformance(String productId, LocalDate from, LocalDate to) {
        return productPerfRepo.findByProductIdAndDateRange(productId, from, to)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public SupplierPerformanceDto getSupplierPerformance(String supplierId, int month, int year) {
        return supplierPerfRepo.findBySupplierIdAndPeriod(supplierId, month, year)
                .map(this::toDto)
                .orElse(null);
    }

    private DailySalesDto toDto(DailySalesSummary model) {
        DailySalesDto dto = new DailySalesDto();
        dto.setSummaryDate(model.getSummaryDate());
        dto.setTotalOrders(model.getTotalOrders());
        dto.setTotalRevenue(model.getTotalRevenue());
        dto.setTotalUnitsSold(model.getTotalUnitsSold());
        dto.setAvgOrderValue(model.getAvgOrderValue());
        dto.setCurrency(model.getCurrency());
        return dto;
    }

    private ProductPerformanceDto toDto(ProductPerformance model) {
        ProductPerformanceDto dto = new ProductPerformanceDto();
        dto.setProductId(model.getProductId());
        dto.setPeriodDate(model.getPeriodDate());
        dto.setViews(model.getViews());
        dto.setPurchases(model.getPurchases());
        dto.setRevenue(model.getRevenue());
        dto.setAvgRating(model.getAvgRating());
        return dto;
    }

    private SupplierPerformanceDto toDto(SupplierPerformance model) {
        SupplierPerformanceDto dto = new SupplierPerformanceDto();
        dto.setSupplierId(model.getSupplierId());
        dto.setPeriodMonth(model.getPeriodMonth());
        dto.setPeriodYear(model.getPeriodYear());
        dto.setTotalOrders(model.getTotalOrders());
        dto.setTotalRevenue(model.getTotalRevenue());
        dto.setAvgRating(model.getAvgRating());
        dto.setCancellationRate(model.getCancellationRate());
        return dto;
    }
}
