package com.homebase.ecom.analytics.service.configuration.controller;

import com.homebase.ecom.analytics.dto.DailySalesDto;
import com.homebase.ecom.analytics.dto.ProductPerformanceDto;
import com.homebase.ecom.analytics.dto.SupplierPerformanceDto;
import com.homebase.ecom.analytics.service.AnalyticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/analytics/sales")
    public ResponseEntity<List<DailySalesDto>> getDailySales(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        List<DailySalesDto> result = analyticsService.getDailySales(from, to);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/analytics/products/{productId}")
    public ResponseEntity<List<ProductPerformanceDto>> getProductPerformance(
            @PathVariable String productId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        List<ProductPerformanceDto> result = analyticsService.getProductPerformance(productId, from, to);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/analytics/suppliers/{supplierId}")
    public ResponseEntity<SupplierPerformanceDto> getSupplierPerformance(
            @PathVariable String supplierId,
            @RequestParam int month,
            @RequestParam int year) {
        SupplierPerformanceDto result = analyticsService.getSupplierPerformance(supplierId, month, year);
        return ResponseEntity.ok(result);
    }
}
