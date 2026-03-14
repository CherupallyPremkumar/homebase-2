package com.homebase.ecom.inventory.service.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.domain.model.Reservation;
import com.homebase.ecom.inventory.domain.model.ReservationStatus;
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
 */
@Component
public class InventoryItemPolicyValidator {

    private static final Logger log = LoggerFactory.getLogger(InventoryItemPolicyValidator.class);

    @Autowired
    private CconfigClient cconfigClient;

    private final ObjectMapper mapper = new ObjectMapper();

    // --- Private: load inventory config root node ---
    private JsonNode getInventoryConfig() {
        try {
            Map<String, Object> map = cconfigClient.value("inventory", null);
            if (map != null) {
                return mapper.valueToTree(map);
            }
        } catch (Exception e) {
            log.warn("Failed to load inventory.json from cconfig, using defaults: {}", e.getMessage());
        }
        return mapper.createObjectNode();
    }

    // ===========================================================
    // POLICY: Reservation
    // ===========================================================

    public void validateReservation(InventoryItem inventory, int requestedQty) {
        JsonNode config = getInventoryConfig();

        // 1. Max quantity per order
        int maxQtyPerOrder = 10; // default
        JsonNode maxQtyNode = config.at("/policies/reservation/maxQuantityPerOrder");
        if (!maxQtyNode.isMissingNode() && maxQtyNode.isInt()) {
            maxQtyPerOrder = maxQtyNode.asInt();
        }
        if (requestedQty > maxQtyPerOrder) {
            throw new ReservationQuantityExceededException(requestedQty, maxQtyPerOrder);
        }

        // 2. Max active reservations
        int maxActiveRes = 50; // default
        JsonNode maxActiveNode = config.at("/policies/reservation/maxActiveReservationsPerProduct");
        if (!maxActiveNode.isMissingNode() && maxActiveNode.isInt()) {
            maxActiveRes = maxActiveNode.asInt();
        }
        long activeCount = inventory.getActiveReservations().stream()
                .filter(r -> r.status() == ReservationStatus.RESERVED)
                .count();
        if (activeCount >= maxActiveRes) {
            throw new MaxReservationsExceededException((int) activeCount, maxActiveRes);
        }
    }

    public int getReservationTtlMinutes() {
        JsonNode config = getInventoryConfig();
        JsonNode ttlNode = config.at("/policies/reservation/reservationTtlMinutes");
        if (!ttlNode.isMissingNode() && ttlNode.isInt()) {
            return ttlNode.asInt();
        }
        return 30; // default
    }

    // ===========================================================
    // POLICY: Stock
    // ===========================================================

    public void validateStockAllocation(InventoryItem inventory, int addedQty) {
        JsonNode config = getInventoryConfig();
        int maxStock = 1000; // default
        JsonNode maxStockNode = config.at("/policies/stock/maxStockPerProduct");
        if (!maxStockNode.isMissingNode() && maxStockNode.isInt()) {
            maxStock = maxStockNode.asInt();
        }
        int currentQty = inventory.getQuantity();
        if (currentQty + addedQty > maxStock) {
            throw new StockCapacityExceededException(currentQty, addedQty, maxStock);
        }
    }

    public void validateApprovalQuantity(int quantity) {
        JsonNode config = getInventoryConfig();
        int minApproval = 1; // default
        JsonNode minNode = config.at("/policies/stock/minApprovalQuantity");
        if (!minNode.isMissingNode() && minNode.isInt()) {
            minApproval = minNode.asInt();
        }
        if (quantity < minApproval) {
            throw new InsufficientStockForApprovalException(quantity, minApproval);
        }
    }

    // ===========================================================
    // POLICY: Damage
    // ===========================================================

    public boolean isSevereDamage(InventoryItem inventory, int damagedQty) {
        JsonNode config = getInventoryConfig();
        int severePercent = 80; // default
        JsonNode severeNode = config.at("/policies/damage/autoDiscardOnSevereDamagePercent");
        if (!severeNode.isMissingNode() && severeNode.isInt()) {
            severePercent = severeNode.asInt();
        }
        int total = inventory.getQuantity();
        if (total == 0) return true; // Avoid division by zero, consider severe if no stock
        int pct = (int) (((double) damagedQty / total) * 100);
        return pct >= severePercent;
    }

    // ===========================================================
    // RULES: Alerts
    // ===========================================================

    public int getLowStockThreshold(InventoryItem inventory) {
        if (inventory.getLowStockThreshold() != null) {
            return inventory.getLowStockThreshold();
        }
        JsonNode config = getInventoryConfig();
        JsonNode threshNode = config.at("/rules/alerts/lowStockThreshold");
        if (!threshNode.isMissingNode() && threshNode.isInt()) {
            return threshNode.asInt();
        }
        return 10;
    }

    public boolean isLowStockAlertEnabled() {
        JsonNode config = getInventoryConfig();
        JsonNode node = config.at("/rules/alerts/lowStockAlertEnabled");
        return node.isMissingNode() || node.asBoolean(true);
    }

    public boolean isOutOfStockAlertEnabled() {
        JsonNode config = getInventoryConfig();
        JsonNode node = config.at("/rules/alerts/outOfStockAlertEnabled");
        return node.isMissingNode() || node.asBoolean(true);
    }

    // ===========================================================
    // RULES: Inspection
    // ===========================================================

    public void validateRejectionComment(String comment) {
        JsonNode config = getInventoryConfig();
        JsonNode node = config.at("/rules/inspection/rejectionRequiresComment");
        boolean required = node.isMissingNode() || node.asBoolean(true);
        if (required && (comment == null || comment.trim().isEmpty())) {
            throw new RejectionCommentRequiredException();
        }
    }

    public boolean isAutoApproveIfNoDefects() {
        JsonNode config = getInventoryConfig();
        JsonNode node = config.at("/rules/inspection/autoApproveIfNoDefects");
        return !node.isMissingNode() && node.asBoolean(false);
    }

    // ===========================================================
    // RULES: Supplier
    // ===========================================================

    public int getMaxProductsPerSupplier() {
        JsonNode config = getInventoryConfig();
        JsonNode node = config.at("/rules/supplier/maxProductsPerSupplier");
        if (!node.isMissingNode() && node.isInt()) {
            return node.asInt();
        }
        return 200;
    }
}
