package com.homebase.ecom.analytics.service;

import com.homebase.ecom.analytics.dto.DailySalesDto;
import com.homebase.ecom.analytics.dto.ProductPerformanceDto;
import com.homebase.ecom.analytics.dto.SupplierPerformanceDto;

import java.time.LocalDate;
import java.util.List;

public interface AnalyticsService {

    List<DailySalesDto> getDailySales(LocalDate from, LocalDate to);

    List<ProductPerformanceDto> getProductPerformance(String productId, LocalDate from, LocalDate to);

    SupplierPerformanceDto getSupplierPerformance(String supplierId, int month, int year);
}
