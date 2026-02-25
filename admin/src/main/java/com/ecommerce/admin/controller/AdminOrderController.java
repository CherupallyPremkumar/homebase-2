package com.ecommerce.admin.controller;

import com.ecommerce.admin.service.AdminOrderService;
import com.ecommerce.admin.service.InvoiceService;
import com.ecommerce.notification.service.NotificationService;
import com.ecommerce.payment.repository.PaymentTransactionRepository;
import com.ecommerce.shared.domain.Order;
import com.ecommerce.admin.service.AuditService;
import com.ecommerce.order.service.ShippingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final AdminOrderService orderService;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final InvoiceService invoiceService;
    private final NotificationService notificationService;
    private final ShippingService shippingService;
    private final AuditService auditService;

    public AdminOrderController(AdminOrderService orderService,
            PaymentTransactionRepository paymentTransactionRepository,
            InvoiceService invoiceService,
            NotificationService notificationService,
            ShippingService shippingService,
            AuditService auditService) {
        this.orderService = orderService;
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.invoiceService = invoiceService;
        this.notificationService = notificationService;
        this.shippingService = shippingService;
        this.auditService = auditService;
    }

    @GetMapping
    public String listOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String searchQuery,
            Model model) {

        Page<Order> orders;
        if (searchQuery != null && !searchQuery.isEmpty()) {
            orders = orderService.searchOrders(searchQuery, PageRequest.of(page, size));
        } else if (status != null && !status.isEmpty()) {
            orders = orderService.getOrdersByStatus(status, PageRequest.of(page, size));
        } else {
            orders = orderService.getAllOrders(PageRequest.of(page, size));
        }

        model.addAttribute("orders", orders.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orders.getTotalPages());
        model.addAttribute("totalItems", orders.getTotalElements());
        model.addAttribute("status", status);
        model.addAttribute("searchQuery", searchQuery);
        return "admin/orders/list";
    }

    @GetMapping("/{orderId}")
    public String orderDetail(@PathVariable String orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);

        orderService.getCustomerForOrder(order.getCustomerId())
                .ifPresent(customer -> model.addAttribute("customer", customer));

        paymentTransactionRepository.findByOrderId(orderId).stream().findFirst()
                .ifPresent(pt -> model.addAttribute("payment", pt));

        shippingService.getShipmentByOrderId(orderId).ifPresent(shipment -> model.addAttribute("shipment", shipment));

        return "admin/orders/detail";
    }

    @PostMapping("/{orderId}/refund")
    public String processRefund(@PathVariable String orderId,
            @RequestParam String reason, RedirectAttributes redirectAttributes) {
        try {
            orderService.refundOrder(orderId, reason);
            auditService.logAction("REFUND_INITIATED", "ORDER", orderId, "Reason: " + reason);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Refund initiated. Order will be marked REFUNDED after gateway webhook confirmation.");
        } catch (Exception e) {
            log.error("Refund failed for order {}: {}", orderId, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Refund failed: " + e.getMessage());
        }
        return "redirect:/admin/orders/" + orderId;
    }

    @PostMapping("/{orderId}/status")
    public String updateOrderStatus(@PathVariable String orderId,
            @RequestParam String status, RedirectAttributes redirectAttributes) {
        orderService.updateOrderStatus(orderId, status);
        auditService.logAction("STATUS_UPDATE", "ORDER", orderId, "New status: " + status);
        redirectAttributes.addFlashAttribute("successMessage", "Status updated to " + status);
        return "redirect:/admin/orders/" + orderId;
    }

    @PostMapping("/{orderId}/email")
    public String sendEmail(@PathVariable String orderId,
            @RequestParam String subject, @RequestParam String body,
            RedirectAttributes redirectAttributes) {
        try {
            notificationService.sendCustomEmail(orderId, subject, body);
            auditService.logAction("EMAIL_SENT", "ORDER", orderId, "Subject: " + subject);
            redirectAttributes.addFlashAttribute("successMessage", "Email sent successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Email failed: " + e.getMessage());
        }
        return "redirect:/admin/orders/" + orderId;
    }

    @PostMapping("/{orderId}/ship")
    public String markAsShipped(@PathVariable String orderId,
            @RequestParam String carrier, @RequestParam String trackingNumber,
            RedirectAttributes redirectAttributes) {
        try {
            shippingService.createShipment(orderId, carrier, trackingNumber);
            orderService.updateOrderStatus(orderId, "SHIPPED");
            notificationService.sendShippingNotification(orderId, trackingNumber);
            auditService.logAction("ORDER_SHIPPED", "ORDER", orderId,
                    "Carrier: " + carrier + ", Tracking: " + trackingNumber);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Order marked as shipped. Customer notified.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Shipping failed: " + e.getMessage());
        }
        return "redirect:/admin/orders/" + orderId;
    }

    @GetMapping("/{orderId}/invoice")
    public void downloadInvoice(@PathVariable String orderId, HttpServletResponse response) throws Exception {
        Order order = orderService.getOrderById(orderId);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "inline; filename=invoice-" + orderId.substring(0, 8) + ".pdf");
        invoiceService.generateInvoice(order, response.getOutputStream());
    }

    @GetMapping("/export/csv")
    public void exportOrdersToCSV(@RequestParam(required = false) String status,
            HttpServletResponse response) throws Exception {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=orders.csv");
        orderService.exportOrdersToCSV(status, response.getWriter());
    }
}
