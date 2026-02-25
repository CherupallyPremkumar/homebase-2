package com.ecommerce.admin.service;

import com.ecommerce.payment.service.impl.PaymentServiceImpl;
import com.ecommerce.shared.domain.Customer;
import com.ecommerce.shared.domain.Order;
import com.ecommerce.order.service.OrderService;
import com.ecommerce.customer.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.Writer;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class AdminOrderService {

    private final OrderService orderService;
    private final PaymentServiceImpl stripePaymentService;
    private final CustomerRepository customerRepository;

    public AdminOrderService(OrderService orderService,
            PaymentServiceImpl stripePaymentService,
            CustomerRepository customerRepository) {
        this.orderService = orderService;
        this.stripePaymentService = stripePaymentService;
        this.customerRepository = customerRepository;
    }

    public Page<Order> getAllOrders(Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }

    public Page<Order> searchOrders(String query, Pageable pageable) {
        return orderService.searchOrders(query, pageable);
    }

    public Page<Order> getOrdersByStatus(String status, Pageable pageable) {
        return orderService.getOrdersByStatus(status, pageable);
    }

    public Order getOrderById(String orderId) {
        return orderService.getOrderById(orderId);
    }

    public void updateOrderStatus(String orderId, String status) {
        orderService.updateOrderStatus(orderId, com.ecommerce.shared.domain.OrderStatus.valueOf(status));
    }

    /**
     * Initiate a refund via the configured gateway.
     *
     * Order status is NOT immediately changed to CANCELLED. The payment gateway webhook will publish
     * PaymentRefundedEvent, and OrderSagaOrchestrator will transition the order to REFUNDED.
     */
    public void refundOrder(String orderId, String reason) {
        Order order = orderService.getOrderById(orderId);
        stripePaymentService.processRefund(orderId, order.getGatewayTransactionId(), order.getTotalAmount(), reason);
    }

    /**
     * Export orders as CSV with full details.
     */
    public void exportOrdersToCSV(String status, Writer writer) throws Exception {
        PrintWriter pw = new PrintWriter(writer);
        pw.println("Order ID,Date,Customer ID,Customer Email,Status,Subtotal,Tax,Shipping,Total");

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<Order> orders;
        if (status != null && !status.isEmpty()) {
            orders = orderService.getOrdersByStatus(status, PageRequest.of(0, 10000)).getContent();
        } else {
            orders = orderService.getAllOrders(PageRequest.of(0, 10000)).getContent();
        }

        for (Order order : orders) {
            String customerEmail = resolveCustomerEmail(order.getCustomerId());
            pw.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%.2f,%.2f,%.2f,%.2f%n",
                    order.getId(),
                    order.getCreatedAt().format(fmt),
                    order.getCustomerId(),
                    customerEmail,
                    order.getStatus(),
                    order.getTotalAmount().subtract(order.getTaxAmount()).subtract(order.getShippingAmount()),
                    order.getTaxAmount(),
                    order.getShippingAmount(),
                    order.getTotalAmount());
        }
        pw.flush();
    }

    public List<Order> getRecentOrders(int limit) {
        return orderService.getRecentOrders(limit);
    }

    public List<Order> getPendingOrders() {
        return orderService.getOrdersByStatus("PROCESSING", PageRequest.of(0, 100)).getContent();
    }

    /**
     * Resolve customer details by ID.
     */
    public Optional<Customer> getCustomerForOrder(String customerId) {
        return customerRepository.findById(customerId);
    }

    private String resolveCustomerEmail(String customerId) {
        return customerRepository.findById(customerId)
                .map(Customer::getEmail)
                .orElse("unknown@unknown.com");
    }
}
