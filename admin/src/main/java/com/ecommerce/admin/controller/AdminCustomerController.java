package com.ecommerce.admin.controller;

import com.ecommerce.admin.service.AdminCustomerService;
import com.ecommerce.shared.domain.Customer;
import com.ecommerce.shared.domain.Order;
import com.ecommerce.customer.repository.CustomerRepository;
import com.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin/customers")
@RequiredArgsConstructor
@Slf4j
public class AdminCustomerController {

    private final AdminCustomerService adminCustomerService;
    private final CustomerRepository customerRepository;
    private final OrderService orderService;

    @GetMapping
    public String listCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        var customersPage = adminCustomerService.getCustomers(page, size);

        model.addAttribute("customers", customersPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", customersPage.getTotalPages());
        model.addAttribute("totalItems", customersPage.getTotalElements());

        return "admin/customers/list";
    }

    @GetMapping("/{customerId}")
    public String customerDetail(@PathVariable String customerId, Model model) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + customerId));

        List<Order> orders = orderService.getCustomerOrders(customerId);

        BigDecimal totalSpend = orders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalOrders = orders.size();
        BigDecimal avgOrderValue = totalOrders > 0
                ? totalSpend.divide(BigDecimal.valueOf(totalOrders), 2, java.math.RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        model.addAttribute("customer", customer);
        model.addAttribute("orders", orders);
        model.addAttribute("totalSpend", totalSpend);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("avgOrderValue", avgOrderValue);

        return "admin/customers/detail";
    }
}
