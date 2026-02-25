package com.ecommerce.admin.controller;

import com.ecommerce.admin.service.AdminAnalyticsService;
import com.ecommerce.payment.batch.GatewayReconciliationDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;

@Slf4j
@Controller
@RequestMapping("/admin/analytics")
public class AdminAnalyticsController {

    private final AdminAnalyticsService analyticsService;
    private final GatewayReconciliationDispatcher reconciliationDispatcher;

    public AdminAnalyticsController(AdminAnalyticsService analyticsService,
            GatewayReconciliationDispatcher reconciliationDispatcher) {
        this.analyticsService = analyticsService;
        this.reconciliationDispatcher = reconciliationDispatcher;
    }

    @GetMapping
    public String analyticsOverview() {
        return "redirect:/admin/analytics/sales";
    }

    @GetMapping("/sales")
    public String salesReport(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            Model model) {

        if (startDate == null)
            startDate = LocalDate.now().minusDays(30);
        if (endDate == null)
            endDate = LocalDate.now();

        var report = analyticsService.generateSalesReport(startDate, endDate);
        model.addAttribute("report", report);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "admin/analytics/sales-report";
    }

    @GetMapping("/revenue")
    public String revenueAnalytics(@RequestParam(defaultValue = "30") int days, Model model) {
        model.addAttribute("revenueTrend", analyticsService.getRevenueTrend(days));
        model.addAttribute("topProducts", analyticsService.getTopProducts(10));
        model.addAttribute("topCustomers", analyticsService.getTopCustomers(10));
        model.addAttribute("days", days);
        return "admin/analytics/revenue-chart";
    }

    @GetMapping("/customers")
    public String customerAnalytics(Model model) {
        model.addAttribute("newCustomers", analyticsService.getNewCustomersCount(30));
        model.addAttribute("returningCustomers", analyticsService.getReturningCustomersCount(30));
        model.addAttribute("averageOrderValue", analyticsService.getAverageOrderValue());
        model.addAttribute("customerLifetimeValue", analyticsService.getAverageCustomerLifetimeValue());
        return "admin/analytics/customer-analytics";
    }

    @GetMapping("/products")
    public String productPerformance(Model model) {
        model.addAttribute("topProducts", analyticsService.getTopProducts(20));
        model.addAttribute("slowMovingProducts", analyticsService.getSlowMovingProducts());
        model.addAttribute("productRevenue", analyticsService.getProductRevenueBreakdown());
        return "admin/analytics/product-performance";
    }

    @GetMapping("/reconciliation")
    public String reconciliation(
            @RequestParam(required = false) String gatewayType,
            @RequestParam(required = false) String mismatchType,
            @RequestParam(required = false) String orderId,
            @RequestParam(required = false) String providerTransactionId,
            Model model) {

        String activeGateway = reconciliationDispatcher.getActiveGatewayType();
        String resolvedGatewayType = (gatewayType == null || gatewayType.isBlank()) ? activeGateway : gatewayType;

        model.addAttribute("activeGateway", activeGateway);
        model.addAttribute("supportedGateways", reconciliationDispatcher.getSupportedGatewayTypes());
        model.addAttribute("gatewayType", resolvedGatewayType);

        model.addAttribute("mismatches", reconciliationDispatcher.getUnresolvedMismatches(resolvedGatewayType, mismatchType, orderId, providerTransactionId));
        model.addAttribute("mismatchType", mismatchType);
        model.addAttribute("orderId", orderId);
        model.addAttribute("providerTransactionId", providerTransactionId);
        return "admin/analytics/reconciliation";
    }

    @PostMapping("/reconciliation/resolve")
    public String resolveMismatch(
            @RequestParam String mismatchId,
            @RequestParam(required = false) String notes,
            @RequestParam(required = false) String gatewayType,
            @RequestParam(required = false) String mismatchType,
            @RequestParam(required = false) String orderId,
            @RequestParam(required = false) String providerTransactionId,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {
            String resolvedBy = principal != null ? principal.getName() : null;
            reconciliationDispatcher.resolveMismatch(mismatchId, notes, resolvedBy);
            redirectAttributes.addFlashAttribute("successMessage", "Mismatch resolved.");
        } catch (Exception e) {
            log.warn("Failed to resolve mismatchId={}", mismatchId, e);
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to resolve mismatch.");
        }

        redirectAttributes.addAttribute("gatewayType", gatewayType);
        redirectAttributes.addAttribute("mismatchType", mismatchType);
        redirectAttributes.addAttribute("orderId", orderId);
        redirectAttributes.addAttribute("providerTransactionId", providerTransactionId);
        return "redirect:/admin/analytics/reconciliation";
    }

    @PostMapping("/reconciliation/trigger")
    public String triggerReconciliation(
            @RequestParam(required = false) String gatewayType,
            RedirectAttributes redirectAttributes) {

        reconciliationDispatcher.triggerReconciliationJob(gatewayType);

        redirectAttributes.addAttribute("gatewayType", gatewayType);
        return "redirect:/admin/analytics/reconciliation";
    }
}
