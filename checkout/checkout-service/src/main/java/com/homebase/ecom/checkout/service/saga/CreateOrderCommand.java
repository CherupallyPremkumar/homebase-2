package com.homebase.ecom.checkout.service.saga;

import com.homebase.ecom.checkout.model.Checkout;
import com.homebase.ecom.checkout.model.CheckoutItem;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.service.OrderService;
import com.homebase.ecom.shared.event.CartCheckoutInitiatedEvent;
import org.chenile.owiz.Command;
import org.chenile.workflow.dto.TransitionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * OWIZ saga step 5: Create an order from checkout data.
 * Uses order-client's OrderService proxy directly.
 * Sets checkout.orderId upon success.
 */
public class CreateOrderCommand implements Command<TransitionContext<Checkout>> {

    private static final Logger log = LoggerFactory.getLogger(CreateOrderCommand.class);

    @Autowired(required = false)
    private OrderService orderServiceClient;

    @Override
    public void execute(TransitionContext<Checkout> context) throws Exception {
        Checkout checkout = context.getEntity();
        if (orderServiceClient != null) {
            CartCheckoutInitiatedEvent event = buildCheckoutEvent(checkout);
            Order order = orderServiceClient.createOrder(event);
            checkout.setOrderId(order.getId());

            log.info("[CHECKOUT SAGA] Order {} created for checkout {}",
                    order.getId(), checkout.getId());
        }
        checkout.setLastCompletedStep("createOrder");
    }

    private CartCheckoutInitiatedEvent buildCheckoutEvent(Checkout checkout) {
        List<CartCheckoutInitiatedEvent.CartItemPayload> items = new ArrayList<>();
        for (CheckoutItem ci : checkout.getItems()) {
            CartCheckoutInitiatedEvent.CartItemPayload item =
                    new CartCheckoutInitiatedEvent.CartItemPayload(ci.getProductId(), ci.getQuantity());
            item.setUnitPrice(ci.getUnitPrice() != null ? ci.getUnitPrice().getAmount() : 0);
            item.setProductName(ci.getProductName());
            item.setSupplierId(ci.getSupplierId());
            items.add(item);
        }

        CartCheckoutInitiatedEvent event = new CartCheckoutInitiatedEvent(
                checkout.getCartId(), checkout.getCustomerId(), items, LocalDateTime.now());
        event.setTotalAmount(checkout.getTotal() != null ? checkout.getTotal().getAmount() : 0);
        event.setDiscountAmount(checkout.getDiscountAmount() != null ? checkout.getDiscountAmount().getAmount() : 0);
        if (checkout.getCouponCodes() != null && !checkout.getCouponCodes().isEmpty()) {
            event.setPromoCode(checkout.getCouponCodes().get(0));
        }
        return event;
    }
}
