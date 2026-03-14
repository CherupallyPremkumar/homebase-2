package com.homebase.ecom.order.service.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homebase.ecom.order.exception.CancellationReasonRequiredException;
import com.homebase.ecom.order.exception.CancellationWindowExpiredException;
import com.homebase.ecom.order.exception.InvalidReturnReasonException;
import com.homebase.ecom.order.exception.ReturnWindowExpiredException;
import org.chenile.cconfig.sdk.CconfigClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Central component enforcing all Order lifecycle policies and returning rule
 * configuration values.
 *
 * <h3>Policies (throw exceptions)</h3>
 * <ul>
 * <li>Cancellation window expiry check</li>
 * <li>Cancellation reason required if policy demands it</li>
 * <li>Return window expiry check</li>
 * <li>Return reason validity</li>
 * </ul>
 *
 * <h3>Rules (return config values)</h3>
 * <ul>
 * <li>Fulfillment SLA: max days, alert hours</li>
 * <li>Audit: comment required on reject/approve return</li>
 * </ul>
 *
 * All values sourced from {@code order.json} via {@code CconfigClient}.
 */
@Component
public class OrderPolicyValidator {

    private static final Logger log = LoggerFactory.getLogger(OrderPolicyValidator.class);

    @Autowired
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
    // POLICY: Cancellation window
    // ===========================================================

    /**
     * Validates that cancellation is initiated within the allowed window.
     * Controlled by: order.json → policies.cancellation.cancellationWindowHours
     *
     * @param orderCreatedAt timestamp when the order was placed
     * @throws CancellationWindowExpiredException if window has passed
     */
    public void validateCancellationWindow(LocalDateTime orderCreatedAt) {
        JsonNode config = getOrderConfig();
        int windowHours = 24;
        JsonNode node = config.at("/policies/cancellation/cancellationWindowHours");
        if (!node.isMissingNode() && node.isInt())
            windowHours = node.asInt();

        long hoursSinceOrder = ChronoUnit.HOURS.between(orderCreatedAt, LocalDateTime.now());
        if (hoursSinceOrder > windowHours) {
            throw new CancellationWindowExpiredException(windowHours);
        }
    }

    /**
     * Validates that a cancellation reason is provided when required.
     * Controlled by: order.json → policies.cancellation.requireCancellationReason
     *
     * @throws CancellationReasonRequiredException if reason missing and policy
     *                                             requires it
     */
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

    /**
     * Validates that return is initiated within the allowed return window.
     * Controlled by: order.json → policies.return.returnWindowDays
     *
     * @param deliveredAt timestamp when the order was delivered
     * @throws ReturnWindowExpiredException if window has passed
     */
    public void validateReturnWindow(LocalDateTime deliveredAt) {
        JsonNode config = getOrderConfig();
        int windowDays = 10;
        JsonNode node = config.at("/policies/return/returnWindowDays");
        if (!node.isMissingNode() && node.isInt())
            windowDays = node.asInt();

        long daysSinceDelivery = ChronoUnit.DAYS.between(deliveredAt, LocalDateTime.now());
        if (daysSinceDelivery > windowDays) {
            throw new ReturnWindowExpiredException(windowDays);
        }
    }

    /**
     * Validates that the return reason is in the platform's allowed list.
     * Controlled by: order.json → policies.return.allowedReturnReasons
     *
     * @throws InvalidReturnReasonException if reason is not allowed
     */
    public void validateReturnReason(String reason) {
        if (reason == null || reason.trim().isEmpty())
            return;
        JsonNode config = getOrderConfig();
        List<String> allowed = new ArrayList<>();
        JsonNode node = config.at("/policies/return/allowedReturnReasons");
        if (!node.isMissingNode() && node.isArray()) {
            node.forEach(r -> allowed.add(r.asText().toUpperCase()));
        }
        if (!allowed.isEmpty() && !allowed.contains(reason.toUpperCase())) {
            throw new InvalidReturnReasonException(reason);
        }
    }

    // ===========================================================
    // RULE: Fulfillment SLA
    // ===========================================================

    /**
     * Returns max fulfillment days from placing order to delivery.
     * Controlled by: order.json → rules.fulfillment.maxFulfillmentDays
     */
    public int getMaxFulfillmentDays() {
        JsonNode node = getOrderConfig().at("/rules/fulfillment/maxFulfillmentDays");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 7;
    }

    /**
     * Returns hours after which an SLA alert should be raised for unfulfilled
     * orders.
     * Controlled by: order.json → rules.fulfillment.slaAlertAfterHours
     */
    public int getSlaAlertAfterHours() {
        JsonNode node = getOrderConfig().at("/rules/fulfillment/slaAlertAfterHours");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 48;
    }

    // ===========================================================
    // RULE: Audit comments
    // ===========================================================

    /**
     * Returns whether a comment is required when rejecting a return request.
     * Controlled by: order.json → rules.audit.requireCommentOnRejectReturn
     */
    public boolean isCommentRequiredOnRejectReturn() {
        JsonNode node = getOrderConfig().at("/rules/audit/requireCommentOnRejectReturn");
        return node.isMissingNode() || node.asBoolean(true);
    }
}
