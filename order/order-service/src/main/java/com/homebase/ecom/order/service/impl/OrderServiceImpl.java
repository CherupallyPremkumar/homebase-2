package com.homebase.ecom.order.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homebase.ecom.order.domain.port.OrderRepository;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.model.OrderItem;
import com.homebase.ecom.order.model.OrderStatus;
import com.homebase.ecom.order.service.OrderService;
import com.homebase.ecom.shared.Money;
import com.homebase.ecom.shared.event.CartCheckoutInitiatedEvent;
import org.chenile.stm.STM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    @Qualifier("orderEntityStm")
    private STM<Order> orderStm;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Transactional
    public Order createOrder(CartCheckoutInitiatedEvent event) throws Exception {
        log.info("Creating order for cart: {}", event.getCartId());

        Order order = new Order();
        order.setCartId(event.getCartId());
        order.setUser_Id(event.getUserId());

        String currency = event.getCurrency() != null ? event.getCurrency() : "USD";
        BigDecimal totalAmount = event.getTotalAmount() != null ? event.getTotalAmount() : BigDecimal.ZERO;
        order.setTotalAmount(new Money(totalAmount, currency));
        order.setStatus(OrderStatus.PENDING);

        if (event.getItems() != null) {
            order.setItems(event.getItems().stream().map(itemInfo -> {
                OrderItem item = new OrderItem();
                item.setProductId(itemInfo.getProductId());
                item.setQuantity(itemInfo.getQuantity());
                item.setProductName(itemInfo.getProductName());

                Money unitPrice = itemInfo.getUnitPrice() != null ? itemInfo.getUnitPrice() : new Money(BigDecimal.ZERO, currency);
                item.setUnitPrice(unitPrice);
                item.setTotalPrice(new Money(unitPrice.getAmount().multiply(BigDecimal.valueOf(item.getQuantity())), currency));

                item.setSupplierId(itemInfo.getSupplierId());
                return item;
            }).collect(Collectors.toList()));
        }

        if (event.getShippingAddress() != null) {
            order.setShippingAddress(objectMapper.writeValueAsString(event.getShippingAddress()));
        }
        if (event.getBillingAddress() != null) {
            order.setBillingAddress(objectMapper.writeValueAsString(event.getBillingAddress()));
        }

        order.setAppliedPromoCode(event.getPromoCode());
        if (event.getDiscountAmount() != null) {
            order.setDiscountAmount(event.getDiscountAmount().doubleValue());
        }

        // Move to initial state CREATED via STM
        orderStm.proceed(order, null, null);
        log.info("Order {} created in CREATED state.", order.getId());

        orderRepository.save(order);
        return order;
    }

    @Override
    public Order getOrder(String orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    @Override
    @Transactional
    public void updateOrder(Order order) {
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void proceed(String orderId, String eventId, Object payload) throws Exception {
        log.info("Transitioning order {} with event {}", orderId, eventId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Order not found: " + orderId));
        orderStm.proceed(order, eventId, payload);
        orderRepository.save(order);
    }
}
