package com.ecommerce.admin.service;

import com.ecommerce.admin.dto.DashboardStatsDto;
import com.ecommerce.admin.dto.RevenueReportDto;
import com.ecommerce.admin.dto.TopProductDto;
import com.ecommerce.admin.dto.RevenueTrendDto;
import com.ecommerce.analytics.service.AnalyticsService;
import com.ecommerce.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminAnalyticsService {

    private final AnalyticsService analyticsService;
    private final OrderRepository orderRepository;

    public AdminAnalyticsService(AnalyticsService analyticsService, OrderRepository orderRepository) {
        this.analyticsService = analyticsService;
        this.orderRepository = orderRepository;
    }

    public DashboardStatsDto getDashboardStats(LocalDateTime start, LocalDateTime end) {
        DashboardStatsDto stats = new DashboardStatsDto();

        // Current period stats
        BigDecimal currentRevenue = orderRepository.getTotalRevenue(start, end);
        if (currentRevenue == null)
            currentRevenue = BigDecimal.ZERO;
        long currentOrders = orderRepository.getTotalOrders(start, end);

        stats.setTotalRevenue(currentRevenue);
        stats.setTotalOrders((int) currentOrders);

        if (currentOrders > 0) {
            stats.setAvgOrderValue(currentRevenue.divide(BigDecimal.valueOf(currentOrders), 2, RoundingMode.HALF_UP));
        } else {
            stats.setAvgOrderValue(BigDecimal.ZERO);
        }

        // Calculate previous period stats for growth percentage
        long durationDays = java.time.Duration.between(start, end).toDays();
        if (durationDays == 0)
            durationDays = 1;

        LocalDateTime prevStart = start.minusDays(durationDays);
        LocalDateTime prevEnd = start;

        BigDecimal prevRevenue = orderRepository.getTotalRevenue(prevStart, prevEnd);
        if (prevRevenue == null)
            prevRevenue = BigDecimal.ZERO;
        long prevOrders = orderRepository.getTotalOrders(prevStart, prevEnd);

        // Revenue Growth
        if (prevRevenue.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal growth = currentRevenue.subtract(prevRevenue)
                    .divide(prevRevenue, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            stats.setRevenueGrowth(String.format("%+.1f%%", growth));
        } else if (currentRevenue.compareTo(BigDecimal.ZERO) > 0) {
            stats.setRevenueGrowth("+100.0%");
        } else {
            stats.setRevenueGrowth("0.0%");
        }

        // Orders Growth
        if (prevOrders > 0) {
            double growth = ((double) (currentOrders - prevOrders) / prevOrders) * 100;
            stats.setOrderGrowth(String.format("%+.1f%%", growth));
        } else if (currentOrders > 0) {
            stats.setOrderGrowth("+100.0%");
        } else {
            stats.setOrderGrowth("0.0%");
        }

        return stats;
    }

    public List<TopProductDto> getTopProducts(int limit) {
        return new ArrayList<>();
    }

    public List<RevenueTrendDto> getRevenueTrend(int days) {
        return new ArrayList<>();
    }

    public RevenueReportDto generateSalesReport(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        RevenueReportDto dto = new RevenueReportDto();

        // Get totals from the existing repository methods
        BigDecimal totalSales = orderRepository.getTotalRevenue(start, end);
        if (totalSales == null)
            totalSales = BigDecimal.ZERO;
        long totalOrders = orderRepository.getTotalOrders(start, end);

        dto.setTotalSales(totalSales);
        dto.setTotalOrders((int) totalOrders);

        if (totalOrders > 0) {
            dto.setAvgOrderValue(totalSales.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP));
        } else {
            dto.setAvgOrderValue(BigDecimal.ZERO);
        }

        // Build daily data by iterating through date range and querying each day
        List<RevenueReportDto.DailySalesDto> dailyData = new ArrayList<>();
        java.util.Set<String> uniqueCustomers = new java.util.HashSet<>();

        // Fetch all paid orders in range
        List<com.ecommerce.shared.domain.Order> orders = orderRepository.findAll().stream()
                .filter(o -> {
                    String status = o.getStatus().name();
                    return (status.equals("PAID") || status.equals("PROCESSING")
                            || status.equals("SHIPPED") || status.equals("DELIVERED"));
                })
                .filter(o -> !o.getCreatedAt().isBefore(start) && !o.getCreatedAt().isAfter(end))
                .toList();

        // Collect unique customers
        orders.forEach(o -> uniqueCustomers.add(o.getCustomerId()));
        dto.setTotalCustomers(uniqueCustomers.size());

        // Group orders by date
        Map<LocalDate, List<com.ecommerce.shared.domain.Order>> ordersByDate = orders.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        o -> o.getCreatedAt().toLocalDate()));

        // Create daily entries for each date in range
        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            RevenueReportDto.DailySalesDto daily = new RevenueReportDto.DailySalesDto();
            daily.setDate(current);

            List<com.ecommerce.shared.domain.Order> dayOrders = ordersByDate.getOrDefault(current, List.of());
            daily.setOrderCount(dayOrders.size());

            BigDecimal daySales = dayOrders.stream()
                    .map(com.ecommerce.shared.domain.Order::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            daily.setSales(daySales);

            if (!dayOrders.isEmpty()) {
                daily.setAvgValue(daySales.divide(BigDecimal.valueOf(dayOrders.size()), 2, RoundingMode.HALF_UP));
            } else {
                daily.setAvgValue(BigDecimal.ZERO);
            }

            dailyData.add(daily);
            current = current.plusDays(1);
        }

        dto.setDailyData(dailyData);
        return dto;
    }

    public long getNewCustomersCount(int days) {
        return 0;
    }

    public long getReturningCustomersCount(int days) {
        return 0;
    }

    public BigDecimal getAverageOrderValue() {
        return BigDecimal.ZERO;
    }

    public BigDecimal getAverageCustomerLifetimeValue() {
        return BigDecimal.ZERO;
    }

    public List<Object> getTopCustomers(int limit) {
        return new ArrayList<>();
    }

    public List<Object> getSlowMovingProducts() {
        return new ArrayList<>();
    }

    public Map<String, BigDecimal> getProductRevenueBreakdown() {
        return new HashMap<>();
    }
}
