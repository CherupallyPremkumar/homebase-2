package com.ecommerce.web.controller;

import com.ecommerce.cart.domain.Cart;
import com.ecommerce.cart.service.CartService;
import com.ecommerce.order.service.OrderService;
import com.ecommerce.payment.dto.CheckoutSessionRequest;
import com.ecommerce.payment.dto.CheckoutSessionResponse;
import com.ecommerce.payment.service.impl.PaymentServiceImpl;
import com.ecommerce.shared.domain.Order;
import com.ecommerce.shared.domain.OrderItem;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/checkout")
@Slf4j
@RequiredArgsConstructor
public class CheckoutPageController {

    private final CartService cartService;
    private final OrderService orderService;
    private final PaymentServiceImpl paymentService;

    @Value("${app.frontend.success-url}")
    private String successUrl;

    @Value("${app.frontend.cancel-url}")
    private String cancelUrl;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @GetMapping
    public String checkoutPage(Authentication auth, HttpSession session, Model model) {
        String customerId = auth.getName();
        model.addAttribute("customerEmail", customerId);

        // Merge guest session cart into user cart if applicable
        String sessionCartKey = "session:" + session.getId();
        cartService.mergeCarts(sessionCartKey, customerId);

        Cart cart = cartService.getCart(customerId);

        if (cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }

        // Convert cart items to order items
        List<OrderItem> orderItems = cart.getItems().stream().map(ci -> {
            OrderItem oi = new OrderItem();
            oi.setProductId(ci.getProductId());
            oi.setProductName(ci.getProductName());
            oi.setQuantity(ci.getQuantity());
            oi.setUnitPrice(ci.getUnitPrice());
            oi.setTotalPrice(ci.getTotalPrice());
            return oi;
        }).collect(Collectors.toList());

        // Create pending order
        Order order = orderService.createPendingOrder(
                customerId, orderItems, cart.getTotal(), cart.getTax(), cart.getShipping());

        // Initialize Checkout session via orchestrator
        try {
            CheckoutSessionRequest request = CheckoutSessionRequest.builder()
                    .orderId(order.getId())
                    .amount(order.getTotalAmount())
                    .currency(order.getCurrency())
                    .successUrl(successUrl)
                    .cancelUrl(cancelUrl)
                    .items(order.getItems().stream().map(item -> CheckoutSessionRequest.LineItem.builder()
                            .productName(item.getProductName())
                            .unitPrice(item.getUnitPrice())
                            .quantity(item.getQuantity())
                            .build()).collect(Collectors.toList()))
                    .build();

            CheckoutSessionResponse response = paymentService.initiateCheckout(request);

            log.info("Checkout session initialized. Redirecting to: {}", response.getCheckoutUrl());

            // Save the gateway session ID to the order
            order.setGatewaySessionId(response.getSessionId());
            orderService.saveOrder(order);

            model.addAttribute("checkoutUrl", response.getCheckoutUrl());
            model.addAttribute("gatewayType", response.getGatewayType());
        } catch (Exception e) {
            log.error("Failed to initialize payment: {}", e.getMessage());
            model.addAttribute("error", "Payment system unavailable. Please try again later.");
        }

        model.addAttribute("order", order);
        model.addAttribute("cart", cart);
        model.addAttribute("razorpayKeyId", razorpayKeyId);
        model.addAttribute("totalAmountPaise", order.getTotalAmount()
                .multiply(new BigDecimal("100"))
                .setScale(0, RoundingMode.HALF_UP)
                .longValue());

        return "checkout/index";
    }

    @GetMapping("/success")
    public String checkoutSuccess(@RequestParam(name = "session_id", required = false) String sessionId,
            @RequestParam(name = "gateway_transaction_id", required = false) String gatewayTransactionId,
            Model model) {
        Order order = null;
        if (sessionId != null) {
            order = orderService.getOrderBySessionId(sessionId);
            // Order status is updated asynchronously via webhook → PaymentSucceededEvent → OrderSaga.
            // We intentionally do NOT try to "force" PAID here, since we may not have enough gateway
            // details (amount / transaction id) on the redirect.
            if (order.getStatus() == com.ecommerce.shared.domain.OrderStatus.PENDING_PAYMENT) {
                log.info("Order {} still PENDING_PAYMENT on success redirect (session {}). Waiting for webhook/event processing.",
                        order.getId(), sessionId);
            }
        } else if (gatewayTransactionId != null) {
            order = orderService.getOrderByPaymentIntent(gatewayTransactionId);
        }

        if (order == null) {
            log.warn("Checkout success page reached but no order found for Session: {} or TX: {}",
                    sessionId, gatewayTransactionId);
            return "redirect:/";
        }

        model.addAttribute("order", order);
        return "checkout/success";
    }

    @GetMapping("/failure")
    public String checkoutFailure(String error_message, Model model) {
        model.addAttribute("error", error_message);
        return "checkout/failure";
    }

    @GetMapping("/cancel")
    public String checkoutCancel() {
        return "checkout/cancel";
    }

    @GetMapping("/resume")
    public String resumeCheckout(@RequestParam String orderId, Authentication auth, Model model) {
        String customerId = auth.getName();
        model.addAttribute("customerEmail", customerId);
        Order order = orderService.getOrder(orderId);

        // Security check: Order must belong to the customer
        if (!order.getCustomerId().equals(customerId)) {
            log.warn("Customer {} attempted to resume payment for order {} belonging to another customer",
                    customerId, orderId);
            return "redirect:/orders";
        }

        // Status check: Order must be in PENDING_PAYMENT
        if (order.getStatus() != com.ecommerce.shared.domain.OrderStatus.PENDING_PAYMENT) {
            log.info("Order {} is already in status {}. Redirecting to detail page.", orderId, order.getStatus());
            return "redirect:/orders/" + orderId;
        }

        // Re-initialize Checkout session
        try {
            CheckoutSessionRequest request = CheckoutSessionRequest.builder()
                    .orderId(order.getId())
                    .amount(order.getTotalAmount())
                    .currency(order.getCurrency())
                    .successUrl(successUrl)
                    .cancelUrl(cancelUrl)
                    .items(order.getItems().stream().map(item -> CheckoutSessionRequest.LineItem.builder()
                            .productName(item.getProductName())
                            .unitPrice(item.getUnitPrice())
                            .quantity(item.getQuantity())
                            .build()).collect(Collectors.toList()))
                    .build();

            CheckoutSessionResponse response = paymentService.initiateCheckout(request);

            log.info("Checkout session re-initialized for order {}. Redirecting to: {}",
                    orderId, response.getCheckoutUrl());

            // Save the gateway session ID to the order
            order.setGatewaySessionId(response.getSessionId());
            orderService.saveOrder(order);

            model.addAttribute("checkoutUrl", response.getCheckoutUrl());
            model.addAttribute("gatewayType", response.getGatewayType());
        } catch (Exception e) {
            log.error("Failed to re-initialize payment for order {}: {}", orderId, e.getMessage());
            model.addAttribute("error", "Payment system unavailable. Please try again later.");
        }

        model.addAttribute("order", order);
        model.addAttribute("razorpayKeyId", razorpayKeyId);
        model.addAttribute("totalAmountPaise", order.getTotalAmount()
                .multiply(new BigDecimal("100"))
                .setScale(0, RoundingMode.HALF_UP)
                .longValue());
        // We don't need a cart here since we're resuming an existing order
        return "checkout/index";
    }
}
