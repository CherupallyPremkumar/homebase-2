package com.homebase.ecom.returnrequest.service.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

/**
 * Central component enforcing all return request policies and rules.
 *
 * <h3>Policies (cconfig: returnrequest.json)</h3>
 * <ul>
 * <li>returnWindowDays (30) - max days after delivery to request return</li>
 * <li>maxReturnItemsPerOrder (10) - max items per return request</li>
 * <li>autoApproveUnderValue (500) - auto-approve returns under this value</li>
 * <li>inspectionRequiredAboveValue (5000) - mandatory inspection above this value</li>
 * <li>restockingFeePercent (0) - restocking fee percentage</li>
 * </ul>
 */
@Component
public class ReturnRequestPolicyValidator {

    private static final Logger log = LoggerFactory.getLogger(ReturnRequestPolicyValidator.class);

    @Autowired(required = false)
    private CconfigClient cconfigClient;

    private final ObjectMapper mapper = new ObjectMapper();

    private JsonNode getReturnConfig() {
        try {
            java.util.Map<String, Object> map = cconfigClient.value("returnrequest", null);
            if (map != null)
                return mapper.valueToTree(map);
        } catch (Exception e) {
            log.warn("Failed to load returnrequest config from cconfig, using defaults: {}", e.getMessage());
        }
        return mapper.createObjectNode();
    }

    // ===========================================================
    // POLICY: Return window (default 30 days)
    // ===========================================================

    public int getReturnWindowDays() {
        JsonNode node = getReturnConfig().at("/policies/return/returnWindowDays");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 30;
    }

    public void validateReturnWindow(LocalDateTime orderDeliveryDate) {
        if (orderDeliveryDate != null) {
            long daysSinceDelivery = ChronoUnit.DAYS.between(orderDeliveryDate, LocalDateTime.now());
            int maxWindow = getReturnWindowDays();
            if (daysSinceDelivery > maxWindow) {
                throw new IllegalArgumentException("Return window expired. Maximum " + maxWindow
                        + " days allowed, but " + daysSinceDelivery + " days have passed since delivery.");
            }
        }
    }

    // ===========================================================
    // POLICY: Max items per order (default 10)
    // ===========================================================

    public int getMaxReturnItemsPerOrder() {
        JsonNode node = getReturnConfig().at("/policies/return/maxReturnItemsPerOrder");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 10;
    }

    public void validateItemCount(int itemCount) {
        int max = getMaxReturnItemsPerOrder();
        if (itemCount > max) {
            throw new IllegalArgumentException("Maximum " + max + " items per return request, but " + itemCount + " requested.");
        }
    }

    // ===========================================================
    // POLICY: Auto-approve under value (default 500)
    // ===========================================================

    public double getAutoApproveUnderValue() {
        JsonNode node = getReturnConfig().at("/policies/approval/autoApproveUnderValue");
        return (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 500.0;
    }

    public boolean canAutoApprove(BigDecimal amount) {
        double threshold = getAutoApproveUnderValue();
        return amount != null && amount.doubleValue() <= threshold;
    }

    // ===========================================================
    // POLICY: Inspection required above value (default 5000)
    // ===========================================================

    public double getInspectionRequiredAboveValue() {
        JsonNode node = getReturnConfig().at("/policies/inspection/inspectionRequiredAboveValue");
        return (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 5000.0;
    }

    public boolean isInspectionRequired(BigDecimal amount) {
        double threshold = getInspectionRequiredAboveValue();
        return amount != null && amount.doubleValue() > threshold;
    }

    // ===========================================================
    // POLICY: Restocking fee percent (default 0)
    // ===========================================================

    public double getRestockingFeePercent() {
        JsonNode node = getReturnConfig().at("/policies/fee/restockingFeePercent");
        return (!node.isMissingNode() && node.isNumber()) ? node.asDouble() : 0.0;
    }

    // ===========================================================
    // VALIDATION: Return reason
    // ===========================================================

    public void validateReturnReason(String reason) {
        if (reason == null || reason.trim().isEmpty()) {
            JsonNode required = getReturnConfig().at("/policies/return/requireReturnReason");
            if (required.isMissingNode() || required.asBoolean(true)) {
                throw new IllegalArgumentException("Return reason is required.");
            }
            return;
        }
        List<String> allowed = new ArrayList<>();
        JsonNode node = getReturnConfig().at("/policies/return/allowedReturnReasons");
        if (!node.isMissingNode() && node.isArray()) {
            node.forEach(r -> allowed.add(r.asText().toUpperCase()));
        }
        if (!allowed.isEmpty() && !allowed.contains(reason.toUpperCase())) {
            throw new IllegalArgumentException(
                    "Return reason '" + reason + "' is not valid. Allowed: " + allowed);
        }
    }
}
