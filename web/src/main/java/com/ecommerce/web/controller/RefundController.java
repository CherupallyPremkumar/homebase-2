package com.ecommerce.web.controller;

import com.ecommerce.order.service.OrderService;
import com.ecommerce.payment.domain.RefundRequest;
import com.ecommerce.payment.repository.RefundRequestRepository;
import com.ecommerce.payment.service.impl.PaymentServiceImpl;
import com.ecommerce.shared.domain.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/refunds")
public class RefundController {

    private final RefundRequestRepository refundRequestRepository;
    private final OrderService orderService;
    private final PaymentServiceImpl paymentService;

    public RefundController(RefundRequestRepository refundRequestRepository,
            OrderService orderService,
            PaymentServiceImpl paymentService) {
        this.refundRequestRepository = refundRequestRepository;
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @PostMapping("/request")
    public String cancelOrder(@RequestParam String orderId,
            @RequestParam String reason,
            RedirectAttributes redirectAttributes) {
        try {
            Order order = orderService.getOrder(orderId);
            String currentStatus = order.getStatus().name();

            // Handle terminal states
            if ("CANCELLED".equals(currentStatus) || "REFUNDED".equals(currentStatus)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Order is already cancelled or refunded.");
                return "redirect:/orders/" + orderId;
            }

            // Segregated Logic: immediate cancel vs. refund initiation vs. request-based return
            if ("PENDING_PAYMENT".equals(currentStatus)) {
                // No money captured yet → cancel immediately
                orderService.cancelOrder(orderId);
                redirectAttributes.addFlashAttribute("successMessage", "Order has been cancelled successfully.");
                return "redirect:/orders/" + orderId;
            }

            if ("PAID".equals(currentStatus) || "PROCESSING".equals(currentStatus)) {
                // Money captured → initiate refund; order status will become REFUNDED only after webhook confirmation
                paymentService.processRefund(orderId, order.getGatewayTransactionId(), order.getTotalAmount(), reason);

                RefundRequest request = new RefundRequest();
                request.setOrderId(orderId);
                request.setAmount(order.getTotalAmount());
                request.setReason(reason);
                request.setStatus("INITIATED");
                refundRequestRepository.save(request);

                redirectAttributes.addFlashAttribute("successMessage",
                        "Refund initiated. Your order will be marked REFUNDED after payment gateway webhook confirmation.");
                return "redirect:/orders/" + orderId;
            }

            if ("SHIPPED".equals(currentStatus) || "DELIVERED".equals(currentStatus)) {
                // Request-based return (admin review)
                RefundRequest request = new RefundRequest();
                request.setOrderId(orderId);
                request.setAmount(order.getTotalAmount());
                request.setReason(reason);
                request.setStatus("PENDING");

                refundRequestRepository.save(request);
                redirectAttributes.addFlashAttribute("successMessage",
                        "Your return/refund request has been submitted and is pending review.");
                return "redirect:/orders/" + orderId;
            }

            redirectAttributes.addFlashAttribute("errorMessage",
                    "Cancellation/Return not allowed for current order status.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Operation failed: " + e.getMessage());
        }

        return "redirect:/orders/" + orderId;
    }
}
