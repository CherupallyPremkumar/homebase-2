package com.ecommerce.admin.controller;

import com.ecommerce.admin.dto.DashboardStatsDto;
import com.ecommerce.admin.service.AdminAnalyticsService;
import com.ecommerce.admin.service.AdminOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final AdminAnalyticsService analyticsService;
    private final AdminOrderService orderService;
    private final com.ecommerce.admin.service.AdminInventoryService inventoryService;

    public AdminDashboardController(AdminAnalyticsService analyticsService,
            AdminOrderService orderService,
            com.ecommerce.admin.service.AdminInventoryService inventoryService) {
        this.analyticsService = analyticsService;
        this.orderService = orderService;
        this.inventoryService = inventoryService;
    }

    /**
     * Dashboard homepage
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            log.info("Accessing admin dashboard");
            // Get monthly stats (30-day window)
            DashboardStatsDto stats = analyticsService.getDashboardStats(
                    LocalDateTime.now().minusDays(30),
                    LocalDateTime.now());

            // Get recent orders
            var recentOrders = orderService.getRecentOrders(10);

            // Get pending orders count
            var pendingOrders = orderService.getPendingOrders();
            stats.setPendingOrders(pendingOrders.size());

            // Get top products
            var topProducts = analyticsService.getTopProducts(5);

            // Get revenue trend (last 7 days)
            var revenueTrend = analyticsService.getRevenueTrend(7);

            model.addAttribute("stats", stats);
            model.addAttribute("recentOrders", recentOrders);
            model.addAttribute("pendingOrders", pendingOrders);
            model.addAttribute("topProducts", topProducts);
            model.addAttribute("revenueTrend", revenueTrend);
            model.addAttribute("lowStockCount", inventoryService.getLowStockCount());

            return "admin/dashboard/index";
        } catch (Exception e) {
            log.error("Error loading admin dashboard", e);
            throw e;
        }
    }

    /**
     * Dashboard stats card (AJAX)
     */
    @GetMapping("/dashboard/stats")
    public String getStats(Model model) {

        DashboardStatsDto stats = analyticsService.getDashboardStats(
                LocalDateTime.now().minusDays(30),
                LocalDateTime.now());

        model.addAttribute("stats", stats);
        return "admin/dashboard/stats";
    }
}
