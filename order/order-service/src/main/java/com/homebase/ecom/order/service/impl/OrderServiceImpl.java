package com.homebase.ecom.order.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.model.OrderItem;
import com.homebase.ecom.order.service.OrderService;
import com.homebase.ecom.order.service.validator.OrderPolicyValidator;
import com.homebase.ecom.shared.event.CartCheckoutInitiatedEvent;
import org.chenile.stm.STM;
import org.chenile.utils.entity.service.EntityStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    @Qualifier("orderEntityStm")
    private STM<Order> orderStm;

    @Autowired
    @Qualifier("orderEntityStore")
    private EntityStore<Order> orderEntityStore;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderPolicyValidator policyValidator;

    @Override
    @Transactional
    public Order createOrder(CartCheckoutInitiatedEvent event) throws Exception {
        log.info("Creating order for cart: {}", event.getCartId());

        Order order = new Order();
        order.setOrderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setCustomerId(event.getUserId());

        String currency = event.getCurrency() != null ? event.getCurrency() : "INR";
        BigDecimal totalAmount = BigDecimal.valueOf(event.getTotalAmount());
        order.setTotalAmount(totalAmount);
        order.setCurrency(currency);

        if (event.getItems() != null) {
            order.setItems(event.getItems().stream().map(itemInfo -> {
                OrderItem item = new OrderItem();
                item.setProductId(itemInfo.getProductId());
                item.setQuantity(itemInfo.getQuantity());
                item.setProductName(itemInfo.getProductName());
                BigDecimal unitPrice = BigDecimal.valueOf(itemInfo.getUnitPrice());
                item.setUnitPrice(unitPrice);
                item.setTotalPrice(unitPrice.multiply(BigDecimal.valueOf(item.getQuantity())));
                return item;
            }).collect(Collectors.toList()));
        }

        if (event.getShippingAddress() != null) {
            order.setShippingAddressId(event.getShippingAddress().toString());
        }
        if (event.getBillingAddress() != null) {
            order.setBillingAddressId(event.getBillingAddress().toString());
        }

        // Validate policy constraints
        policyValidator.validateOrderValue(order);
        policyValidator.validateItemCount(order);

        orderStm.proceed(order, null, null);
        log.info("Order {} created in CREATED state.", order.getId());
        return order;
    }

    @Override
    public Order getOrder(String orderId) {
        return orderEntityStore.retrieve(orderId);
    }

    @Override
    @Transactional
    public void updateOrder(Order order) {
        orderEntityStore.store(order);
    }

    @Override
    @Transactional
    public void proceed(String orderId, String eventId, Object payload) throws Exception {
        log.info("Transitioning order {} with event {}", orderId, eventId);
        Order order = orderEntityStore.retrieve(orderId);
        if (order == null) throw new Exception("Order not found: " + orderId);
        orderStm.proceed(order, eventId, payload);
    }
}
