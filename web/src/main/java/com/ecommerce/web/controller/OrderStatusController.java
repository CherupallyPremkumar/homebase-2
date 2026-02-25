package com.ecommerce.web.controller;

import com.ecommerce.order.service.OrderService;
import com.ecommerce.payment.repository.PaymentTransactionRepository;
import com.ecommerce.shared.domain.Order;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderStatusController {

    private final OrderService orderService;
    private final PaymentTransactionRepository paymentTransactionRepository;

    public OrderStatusController(OrderService orderService, PaymentTransactionRepository paymentTransactionRepository) {
        this.orderService = orderService;
        this.paymentTransactionRepository = paymentTransactionRepository;
    }

    @GetMapping
    public String listOrders(Authentication auth, Model model) {
        List<Order> orders = orderService.getCustomerOrders(auth.getName());
        model.addAttribute("orders", orders);
        return "orders/list";
    }

    @GetMapping("/{orderId}")
    public String orderDetail(@PathVariable String orderId, Model model) {
        Order order = orderService.getOrder(orderId);
        model.addAttribute("order", order);

        // Fetch and attach payment details if available
        paymentTransactionRepository.findByOrderId(orderId).stream().findFirst().ifPresent(paymentTransaction -> {
            model.addAttribute("payment", paymentTransaction);
        });

        return "orders/detail";
    }
}
