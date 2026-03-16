package com.homebase.ecom.inventory.service.validator;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.domain.model.Reservation;
import com.homebase.ecom.inventory.domain.model.ReservationStatus;
import com.homebase.ecom.inventory.domain.port.InventoryPolicyPort;
import com.homebase.ecom.inventory.service.exception.*;
import org.chenile.cconfig.sdk.CconfigClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Central component for enforcing all inventory policies and reading rule
 * configurations.
 *
 * <p>Two-layer validation:
 * <ul>
 *   <li><b>Layer 1 — Cconfig thresholds:</b> reads configurable rules from
 *       {@code org/chenile/config/inventory.json} (overridable per tenant in DB).
 *       Used for field-level checks (max quantity, thresholds, etc.).</li>
 *   <li><b>Layer 2 — Policy engine:</b> delegates to the external Policy BC via
 *       {@link InventoryPolicyPort} for complex, cross-cutting rules.
 *       The policyId is resolved from cconfig ({@code policyEngine.<action>})
 *       so it's tenant-customizable.</li>
 * </ul>
 */
@Component
public class InventoryItemPolicyValidator {

    private static final Logger log = LoggerFactory.getLogger(InventoryItemPolicyValidator.class);

    @Autowired(required = false)
    private CconfigClient cconfigClient;

    @Autowired(required = false)
    private InventoryPolicyPort inventoryPolicyPort;

    private final ObjectMapper mapper = new ObjectMapper();

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Reservation
    // ═══════════════════════════════════════════════════════════════════════

    public void validateReservation(InventoryItem inventory, int requestedQty) {
        JsonNode config = getInventoryConfig();

        int maxQtyPerOrder = configInt(config, "/policies/reservation/maxQuantityPerOrder", 10);
        if (requestedQty > maxQtyPerOrder) {
            throw new ReservationQuantityExceededException(requestedQty, maxQtyPerOrder);
        }

        int maxActiveRes = configInt(config, "/policies/reservation/maxActiveReservationsPerProduct", 50);
        long activeCount = inventory.getActiveReservations().stream()
                .filter(r -> r.status() == ReservationStatus.RESERVED)
                .count();
        if (activeCount >= maxActiveRes) {
            throw new MaxReservationsExceededException((int) activeCount, maxActiveRes);
        }

        evaluatePolicyEngine("inventory-reserve-stock", inventory, config);
    }

    public int getReservationTtlMinutes() {
        JsonNode config = getInventoryConfig();
        return configInt(config, "/policies/reservation/reservationTtlMinutes", 30);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Stock
    // ═══════════════════════════════════════════════════════════════════════

    public void validateStockAllocation(InventoryItem inventory, int addedQty) {
        JsonNode config = getInventoryConfig();
        int maxStock = configInt(config, "/policies/stock/maxStockPerProduct", 1000);
        int currentQty = inventory.getQuantity();
        if (currentQty + addedQty > maxStock) {
            throw new StockCapacityExceededException(currentQty, addedQty, maxStock);
        }
    }

    public void validateApprovalQuantity(InventoryItem inventory, int quantity) {
        JsonNode config = getInventoryConfig();
        int minApproval = configInt(config, "/policies/stock/minApprovalQuantity", 1);
        if (quantity < minApproval) {
            throw new InsufficientStockForApprovalException(quantity, minApproval);
        }

        evaluatePolicyEngine("inventory-approve-stock", inventory, config);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Damage
    // ═══════════════════════════════════════════════════════════════════════

    public boolean isSevereDamage(InventoryItem inventory, int damagedQty) {
        JsonNode config = getInventoryConfig();
        int severePercent = configInt(config, "/policies/damage/autoDiscardOnSevereDamagePercent", 80);
        int total = inventory.getQuantity();
        if (total == 0) return true;
        int pct = (int) (((double) damagedQty / total) * 100);

        evaluatePolicyEngine("inventory-damage-found", inventory, config);

        return pct >= severePercent;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // RULES: Alerts
    // ═══════════════════════════════════════════════════════════════════════

    public int getLowStockThreshold(InventoryItem inventory) {
        if (inventory.getLowStockThreshold() != null) {
            return inventory.getLowStockThreshold();
        }
        JsonNode config = getInventoryConfig();
        return configInt(config, "/rules/alerts/lowStockThreshold", 10);
    }

    public boolean isLowStockAlertEnabled() {
        JsonNode config = getInventoryConfig();
        return configBool(config, "/rules/alerts/lowStockAlertEnabled", true);
    }

    public boolean isOutOfStockAlertEnabled() {
        JsonNode config = getInventoryConfig();
        return configBool(config, "/rules/alerts/outOfStockAlertEnabled", true);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // RULES: Inspection
    // ═══════════════════════════════════════════════════════════════════════

    public void validateRejectionComment(String comment) {
        JsonNode config = getInventoryConfig();
        boolean required = configBool(config, "/policies/inspection/rejectionRequiresComment", true);
        if (required && (comment == null || comment.trim().isEmpty())) {
            throw new RejectionCommentRequiredException();
        }
    }

    public boolean isAutoApproveIfNoDefects() {
        JsonNode config = getInventoryConfig();
        return configBool(config, "/rules/inspection/autoApproveIfNoDefects", false);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // RULES: Supplier
    // ═══════════════════════════════════════════════════════════════════════

    public int getMaxProductsPerSupplier() {
        JsonNode config = getInventoryConfig();
        return configInt(config, "/rules/supplier/maxProductsPerSupplier", 200);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // INTERNAL: Policy engine delegation
    // ═══════════════════════════════════════════════════════════════════════

    private void evaluatePolicyEngine(String action, InventoryItem inventoryItem, JsonNode config) {
        if (inventoryPolicyPort == null) {
            return;
        }

        String policyId = configString(config, "/policyEngine/" + action, null);

        if (policyId != null) {
            Map<String, Object> context = Map.of("policyId", policyId);
            InventoryPolicyPort.PolicyDecision decision = inventoryPolicyPort.evaluate(action, inventoryItem, context);
            if (!decision.allowed()) {
                log.warn("Policy engine denied action '{}': {}", action, decision.reason());
                throw new IllegalArgumentException("Policy check failed: " + decision.reason());
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // INTERNAL: Config helpers
    // ═══════════════════════════════════════════════════════════════════════

    private JsonNode getInventoryConfig() {
        try {
            if (cconfigClient != null) {
                Map<String, Object> map = cconfigClient.value("inventory", null);
                if (map != null) {
                    return mapper.valueToTree(map);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to load inventory.json from cconfig, using defaults: {}", e.getMessage());
        }
        return mapper.createObjectNode();
    }

    private int configInt(JsonNode config, String path, int defaultVal) {
        JsonNode node = config.at(path);
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : defaultVal;
    }

    private boolean configBool(JsonNode config, String path, boolean defaultVal) {
        JsonNode node = config.at(path);
        return node.isMissingNode() ? defaultVal : node.asBoolean(defaultVal);
    }

    private String configString(JsonNode config, String path, String defaultVal) {
        JsonNode node = config.at(path);
        return (!node.isMissingNode() && node.isTextual()) ? node.asText() : defaultVal;
    }
}
