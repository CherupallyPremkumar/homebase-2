package com.homebase.ecom.order.service.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homebase.ecom.order.exception.CancellationReasonRequiredException;
import com.homebase.ecom.order.exception.CancellationWindowExpiredException;
import com.homebase.ecom.order.exception.InvalidReturnReasonException;
import com.homebase.ecom.order.exception.ReturnWindowExpiredException;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.cconfig.sdk.CconfigClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Central component enforcing all Order lifecycle policies.
 * All values sourced from order.json via CconfigClient.
 *
 * Policies:
 * - Order value limits (min/max)
 * - Item count limit
 * - Cancellation window expiry
 * - Cancellation reason required
 * - Return window expiry
 * - Return reason validity
 */
@Component
public class OrderPolicyValidator {

    private static final Logger log = LoggerFactory.getLogger(OrderPolicyValidator.class);

    @Autowired(required = false)
    private CconfigClient cconfigClient;

    private final ObjectMapper mapper = new ObjectMapper();

    private JsonNode getOrderConfig() {
        try {
            Map<String, Object> map = cconfigClient.value("order", null);
            if (map != null) {
                return mapper.valueToTree(map);
            }
        } catch (Exception e) {
            log.warn("Failed to load order.json from cconfig, using defaults: {}", e.getMessage());
        }
        return mapper.createObjectNode();
    }

    // ===========================================================
    // POLICY: Order value limits
    // ===========================================================

    /**
     * Validates that order total is within configured min/max bounds.
     */
    public void validateOrderValue(Order order) {
        if (order.getTotalAmount() == null) return;

        JsonNode config = getOrderConfig();
        int maxValue = getInt(config, "/policies/order/maxOrderValue", 500000);
        int minValue = getInt(config, "/policies/order/minOrderValue", 100);

        BigDecimal total = order.getTotalAmount();
        if (total.compareTo(BigDecimal.valueOf(maxValue)) > 0) {
            throw new IllegalStateException("Order total " + total + " exceeds maximum allowed value " + maxValue);
        }
        if (total.compareTo(BigDecimal.valueOf(minValue)) < 0) {
            throw new IllegalStateException("Order total " + total + " is below minimum required value " + minValue);
        }
    }

    /**
     * Validates that order item count is within limits.
     */
    public void validateItemCount(Order order) {
        if (order.getItems() == null) return;

        JsonNode config = getOrderConfig();
        int maxItems = getInt(config, "/policies/order/maxItemsPerOrder", 50);

        if (order.getItems().size() > maxItems) {
            throw new IllegalStateException("Order has " + order.getItems().size() +
                    " items, exceeds maximum of " + maxItems);
        }
    }

    /**
     * Validates order has items.
     */
    public void validateOrderHasItems(Order order) {
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot process: order " + order.getId() + " has no items");
        }
    }

    // ===========================================================
    // POLICY: Cancellation window
    // ===========================================================

    public void validateCancellationWindow(LocalDateTime orderCreatedAt) {
        JsonNode config = getOrderConfig();
        int windowHours = getInt(config, "/policies/cancellation/cancellationWindowHours", 24);

        long hoursSinceOrder = ChronoUnit.HOURS.between(orderCreatedAt, LocalDateTime.now());
        if (hoursSinceOrder > windowHours) {
            throw new CancellationWindowExpiredException(windowHours);
        }
    }

    public void validateCancellationReason(String reason) {
        JsonNode config = getOrderConfig();
        JsonNode required = config.at("/policies/cancellation/requireCancellationReason");
        if (required.isMissingNode() || required.asBoolean(true)) {
            if (reason == null || reason.trim().isEmpty()) {
                throw new CancellationReasonRequiredException();
            }
        }
    }

    // ===========================================================
    // POLICY: Return window
    // ===========================================================

    public void validateReturnWindow(LocalDateTime deliveredAt) {
        JsonNode config = getOrderConfig();
        int windowDays = getInt(config, "/policies/refund/returnWindowDays", 10);

        long daysSinceDelivery = ChronoUnit.DAYS.between(deliveredAt, LocalDateTime.now());
        if (daysSinceDelivery > windowDays) {
            throw new ReturnWindowExpiredException(windowDays);
        }
    }

    public void validateReturnReason(String reason) {
        if (reason == null || reason.trim().isEmpty()) return;
        JsonNode config = getOrderConfig();
        List<String> allowed = new ArrayList<>();
        JsonNode node = config.at("/policies/refund/allowedReturnReasons");
        if (!node.isMissingNode() && node.isArray()) {
            node.forEach(r -> allowed.add(r.asText().toUpperCase()));
        }
        if (!allowed.isEmpty() && !allowed.contains(reason.toUpperCase())) {
            throw new InvalidReturnReasonException(reason);
        }
    }

    // ===========================================================
    // RULES
    // ===========================================================

    public int getMaxFulfillmentDays() {
        return getInt(getOrderConfig(), "/rules/fulfillment/maxFulfillmentDays", 7);
    }

    public int getSlaAlertAfterHours() {
        return getInt(getOrderConfig(), "/rules/fulfillment/slaAlertAfterHours", 48);
    }

    public boolean isCommentRequiredOnRejectReturn() {
        JsonNode node = getOrderConfig().at("/rules/audit/requireCommentOnRejectReturn");
        return node.isMissingNode() || node.asBoolean(true);
    }

    public int getCancellationWindowHours() {
        return getInt(getOrderConfig(), "/policies/order/cancellationWindowHours", 24);
    }

    private int getInt(JsonNode config, String path, int defaultValue) {
        JsonNode node = config.at(path);
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : defaultValue;
    }
}
