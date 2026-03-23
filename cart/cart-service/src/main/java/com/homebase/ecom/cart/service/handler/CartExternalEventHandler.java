package com.homebase.ecom.cart.service.handler;

import com.homebase.ecom.checkout.api.event.CheckoutCompensatedEventDto;
import com.homebase.ecom.checkout.api.event.CheckoutCompletedEventDto;
import com.homebase.ecom.core.workflow.HmStateEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Handles incoming cross-BC events and translates them into cart STM transitions.
 *
 * Replaces the cart-jobs translators. Receives raw event JSON from
 * checkout.events, inventory.events, product.events, promo.events
 * and calls processById on the cart service for each affected cart.
 *
 * Fan-out events (stock depleted, coupon expired) query the CartQueryPort
 * to find affected carts.
 */
public class CartExternalEventHandler {

    private static final Logger log = LoggerFactory.getLogger(CartExternalEventHandler.class);

    private final CartQueryPort cartQueryPort;
    private final ObjectMapper objectMapper;

    public CartExternalEventHandler(CartQueryPort cartQueryPort, ObjectMapper objectMapper) {
        this.cartQueryPort = cartQueryPort;
        this.objectMapper = objectMapper;
    }

    public void handle(String eventPayload, HmStateEntityServiceImpl<?> cartService) {
        try {
            JsonNode event = objectMapper.readTree(eventPayload);
            String eventType = event.has("eventType") ? event.get("eventType").asText() : null;

            if (eventType == null) {
                log.warn("Received event without eventType, skipping: {}", eventPayload);
                return;
            }

            switch (eventType) {
                case CheckoutCompletedEventDto.EVENT_TYPE -> handleCheckoutCompleted(event, cartService);
                case CheckoutCompensatedEventDto.EVENT_TYPE -> handleCheckoutCompensated(event, cartService);
                case "STOCK_DEPLETED" -> handleStockDepleted(event, cartService);
                case "PRODUCT_DISCONTINUED" -> handleProductDiscontinued(event, cartService);
                case "PRICE_CHANGED" -> handlePriceChanged(event, cartService);
                case "COUPON_EXPIRED" -> handleCouponExpired(event, cartService);
                default -> log.debug("Ignoring event type: {}", eventType);
            }
        } catch (Exception e) {
            log.error("Failed to handle external event: {}", eventPayload, e);
        }
    }

    private void handleCheckoutCompleted(JsonNode event, HmStateEntityServiceImpl<?> cartService) {
        String cartId = textOrNull(event, "cartId");
        String orderId = textOrNull(event, "orderId");
        if (cartId == null || orderId == null) {
            log.warn("CHECKOUT_COMPLETED missing cartId or orderId, skipping");
            return;
        }
        log.info("Checkout completed for cartId={}, orderId={}", cartId, orderId);
        triggerEvent(cartService, cartId, "completeCheckout", "{\"orderId\":\"" + orderId + "\"}");
    }

    private void handleCheckoutCompensated(JsonNode event, HmStateEntityServiceImpl<?> cartService) {
        String cartId = textOrNull(event, "cartId");
        if (cartId == null) {
            log.warn("CHECKOUT_COMPENSATED missing cartId, skipping");
            return;
        }
        String reason = textOrNull(event, "reason");
        log.info("Checkout compensated for cartId={}, reason={}", cartId, reason);
        triggerEvent(cartService, cartId, "cancelCheckout", "{}");
    }

    private void handleStockDepleted(JsonNode event, HmStateEntityServiceImpl<?> cartService) {
        String variantId = textOrNull(event, "variantId");
        if (variantId == null) {
            log.warn("STOCK_DEPLETED missing variantId, skipping");
            return;
        }
        log.info("Stock depleted for variantId={}", variantId);
        List<String> cartIds = cartQueryPort.findActiveCartsWithVariant(variantId);
        String payload = "{\"variantId\":\"" + variantId + "\"}";
        fanOut(cartService, cartIds, "flagUnavailable", payload);
    }

    private void handleProductDiscontinued(JsonNode event, HmStateEntityServiceImpl<?> cartService) {
        String productId = textOrNull(event, "productId");
        if (productId == null) {
            log.warn("PRODUCT_DISCONTINUED missing productId, skipping");
            return;
        }
        log.info("Product discontinued: {}", productId);
        List<String> cartIds = cartQueryPort.findActiveCartsWithVariant(productId);
        String payload = "{\"productId\":\"" + productId + "\"}";
        fanOut(cartService, cartIds, "flagUnavailable", payload);
    }

    private void handlePriceChanged(JsonNode event, HmStateEntityServiceImpl<?> cartService) {
        String variantId = textOrNull(event, "variantId");
        if (variantId == null) {
            log.warn("PRICE_CHANGED missing variantId, skipping");
            return;
        }
        log.info("Price changed for variantId={}", variantId);
        List<String> cartIds = cartQueryPort.findActiveCartsWithVariant(variantId);
        fanOut(cartService, cartIds, "refreshPricing", "{}");
    }

    private void handleCouponExpired(JsonNode event, HmStateEntityServiceImpl<?> cartService) {
        String couponCode = textOrNull(event, "couponCode");
        if (couponCode == null) {
            log.warn("COUPON_EXPIRED missing couponCode, skipping");
            return;
        }
        log.info("Coupon expired: {}", couponCode);
        List<String> cartIds = cartQueryPort.findActiveCartsWithCoupon(couponCode);
        String payload = "{\"couponCode\":\"" + couponCode + "\"}";
        fanOut(cartService, cartIds, "expireCoupon", payload);
    }

    private void triggerEvent(HmStateEntityServiceImpl<?> cartService, String cartId, String eventId, String payload) {
        try {
            cartService.processById(cartId, eventId, payload);
        } catch (Exception e) {
            log.error("Failed to trigger {} for cartId={}", eventId, cartId, e);
        }
    }

    private void fanOut(HmStateEntityServiceImpl<?> cartService, List<String> cartIds, String eventId, String payload) {
        if (cartIds == null || cartIds.isEmpty()) {
            log.debug("No carts affected for event {}", eventId);
            return;
        }
        log.info("Fan-out: triggering {} for {} carts", eventId, cartIds.size());
        for (String cartId : cartIds) {
            triggerEvent(cartService, cartId, eventId, payload);
        }
    }

    private String textOrNull(JsonNode node, String field) {
        return node.has(field) && !node.get(field).isNull() ? node.get(field).asText() : null;
    }

    /**
     * Port for querying cart read model to find affected carts.
     */
    public interface CartQueryPort {
        List<String> findActiveCartsWithVariant(String variantId);
        List<String> findActiveCartsWithCoupon(String couponCode);
    }
}
