package com.homebase.ecom.checkout.infrastructure.integration;

import com.homebase.ecom.checkout.domain.port.OrderCreationPort;
import com.homebase.ecom.checkout.model.Checkout;
import com.homebase.ecom.checkout.model.CheckoutItem;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.service.OrderService;
import com.homebase.ecom.shared.event.CartCheckoutInitiatedEvent;
import com.homebase.ecom.shared.event.CartCheckoutInitiatedEvent.CartItemPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Driven adapter: creates/cancels orders via Order service.
 * Delegates to order-client's OrderService proxy (local in monolith, HTTP in microservices).
 *
 * Translates Checkout domain model to CartCheckoutInitiatedEvent for order creation,
 * and uses OrderService.proceed() for cancellation via STM event.
 */
public class OrderCreationAdapter implements OrderCreationPort {

    private static final Logger log = LoggerFactory.getLogger(OrderCreationAdapter.class);

    private final OrderService orderService;

    public OrderCreationAdapter(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String createOrder(Checkout checkout) {
        log.info("Creating order from checkout={}, customerId={}",
                checkout.getId(), checkout.getCustomerId());
        // TODO: delegate to order-client create endpoint
        try {
            CartCheckoutInitiatedEvent event = toCheckoutEvent(checkout);
            Order order = orderService.createOrder(event);
            String orderId = order.getId();
            log.info("Order created successfully: orderId={} from checkout={}", orderId, checkout.getId());
            return orderId;
        } catch (Exception e) {
            log.error("Failed to create order from checkout={}: {}", checkout.getId(), e.getMessage());
            throw new RuntimeException("Order creation failed for checkout=" + checkout.getId(), e);
        }
    }

    @Override
    public void cancelOrder(String orderId) {
        log.info("Cancelling order={} (compensation)", orderId);
        // TODO: delegate to order-client cancel endpoint
        try {
            orderService.proceed(orderId, "CANCEL", null);
            log.info("Order cancelled successfully: orderId={}", orderId);
        } catch (Exception e) {
            log.error("Failed to cancel order={}: {}", orderId, e.getMessage());
            throw new RuntimeException("Order cancellation failed for orderId=" + orderId, e);
        }
    }

    // -- Checkout -> CartCheckoutInitiatedEvent translation --

    private CartCheckoutInitiatedEvent toCheckoutEvent(Checkout checkout) {
        List<CartItemPayload> itemPayloads = checkout.getItems().stream()
                .map(this::toCartItemPayload)
                .collect(Collectors.toList());

        CartCheckoutInitiatedEvent event = new CartCheckoutInitiatedEvent(
                checkout.getCartId(),
                checkout.getCustomerId(),
                itemPayloads,
                LocalDateTime.now()
        );
        event.setCurrency(checkout.getTotal() != null ? checkout.getTotal().getCurrency() : "INR");
        event.setTotalAmount(checkout.getTotal() != null ? checkout.getTotal().getAmount() : 0L);
        event.setDiscountAmount(checkout.getDiscountAmount() != null ? checkout.getDiscountAmount().getAmount() : 0L);
        if (checkout.getCouponCodes() != null && !checkout.getCouponCodes().isEmpty()) {
            event.setPromoCode(checkout.getCouponCodes().get(0));
        }
        return event;
    }

    private CartItemPayload toCartItemPayload(CheckoutItem item) {
        CartItemPayload payload = new CartItemPayload();
        payload.setProductId(item.getProductId());
        payload.setQuantity(item.getQuantity());
        payload.setUnitPrice(item.getUnitPrice() != null ? item.getUnitPrice().getAmount() : 0L);
        payload.setProductName(item.getProductName());
        payload.setSupplierId(item.getSupplierId());
        return payload;
    }
}
