package com.homebase.ecom.product.service.validator;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.domain.port.PimPolicyPort;
import com.homebase.ecom.product.service.exception.*;
import org.chenile.cconfig.sdk.CconfigClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Central component for enforcing PIM (Product Information Management) policies.
 *
 * <p>Two-layer validation:
 * <ul>
 *   <li><b>Layer 1 — Cconfig thresholds:</b> reads configurable rules from
 *       {@code org/chenile/config/product.json} (overridable per tenant in DB).
 *       Used for field-level checks (min name length, min images, etc.).</li>
 *   <li><b>Layer 2 — Policy engine:</b> delegates to the external Policy BC via
 *       {@link PimPolicyPort} for complex, cross-cutting SpEL rules.
 *       The policyId is resolved from cconfig ({@code policyEngine.<action>})
 *       so it's tenant-customizable.</li>
 * </ul>
 */
@Component
public class ProductPolicyValidator {

    private static final Logger log = LoggerFactory.getLogger(ProductPolicyValidator.class);

    @Autowired(required = false)
    private CconfigClient cconfigClient;

    @Autowired(required = false)
    private PimPolicyPort pimPolicyPort;

    private final ObjectMapper mapper = new ObjectMapper();

    private JsonNode getProductConfig() {
        try {
            if (cconfigClient != null) {
                Map<String, Object> map = cconfigClient.value("product", null);
                if (map != null) {
                    return mapper.valueToTree(map);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to load product config from cconfig, using defaults: {}", e.getMessage());
        }
        return mapper.createObjectNode();
    }

    /**
     * POLICY: Lifecycle — Submit for Review.
     * Validates name, description, and category against cconfig thresholds,
     * then optionally delegates to the policy engine.
     */
    public void validateSubmitForReview(Product product) {
        JsonNode config = getProductConfig();

        int minNameLen = configInt(config, "/policies/submitForReview/minNameLength", 1);
        int minDescLen = configInt(config, "/policies/submitForReview/minDescriptionLength", 1);
        boolean catRequired = configBool(config, "/policies/submitForReview/categoryRequired", false);

        if (product.getName() == null || product.getName().trim().length() < minNameLen) {
            log.warn("Submit blocked: product name is missing or too short (min {})", minNameLen);
            throw new NameRequiredForSubmitException();
        }

        if (product.getDescription() == null || product.getDescription().trim().length() < minDescLen) {
            log.warn("Submit blocked: product description is missing or too short (min {})", minDescLen);
            throw new DescriptionRequiredForSubmitException();
        }

        if (catRequired && (product.getCategoryId() == null || product.getCategoryId().trim().isEmpty())) {
            log.warn("Submit blocked: product category is missing");
            throw new CategoryRequiredForSubmitException();
        }

        evaluatePolicyEngine("pim-submit-review", product, config);
    }

    /**
     * POLICY: Lifecycle — Publish (Approve).
     * Validates images, description, brand, variants against cconfig thresholds,
     * then optionally delegates to the policy engine.
     */
    public void validatePublish(Product product) {
        JsonNode config = getProductConfig();
        String productId = product.getId() != null ? product.getId().toString() : product.getName();

        int minImages = configInt(config, "/policies/publish/minImages", 0);
        int minDescLen = configInt(config, "/policies/publish/minDescriptionLength", 0);
        boolean brandRequired = configBool(config, "/policies/publish/brandRequired", false);
        int minVariants = configInt(config, "/policies/publish/minVariants", 0);

        if (product.getDescription() == null || product.getDescription().trim().length() < minDescLen) {
            log.warn("Publish blocked: description too short for {} (min {})", productId, minDescLen);
            throw new DescriptionRequiredForPublishException(productId);
        }

        if (product.getMedia() == null || product.getMedia().size() < minImages) {
            log.warn("Publish blocked: not enough images for {} (min {})", productId, minImages);
            throw new ImageRequiredForPublishException(productId);
        }

        if (brandRequired && (product.getBrand() == null || product.getBrand().trim().isEmpty())) {
            log.warn("Publish blocked: brand is missing for {}", productId);
            throw new IllegalArgumentException("Brand is required for publish");
        }

        if (product.getVariants() == null || product.getVariants().size() < minVariants) {
            log.warn("Publish blocked: not enough variants for {} (min {})", productId, minVariants);
            throw new IllegalArgumentException("At least " + minVariants + " variant is required for publish");
        }

        evaluatePolicyEngine("pim-publish-approved", product, config);
    }

    /**
     * POLICY: Lifecycle — Rejection comment.
     */
    public void validateRejectionComment(String comment) {
        JsonNode config = getProductConfig();
        boolean required = configBool(config, "/policies/lifecycle/rejectionRequiresComment", true);
        int minLen = configInt(config, "/policies/lifecycle/minRejectionCommentLength", 10);

        if (required && (comment == null || comment.trim().length() < minLen)) {
            throw new ProductRejectionCommentRequiredException();
        }
    }

    /**
     * RULE: Quality — auto-reject threshold score.
     */
    public int getAutoRejectBelowScore() {
        return 30;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // INTERNAL: Policy engine delegation
    // ═══════════════════════════════════════════════════════════════════════

    private void evaluatePolicyEngine(String action, Product product, JsonNode config) {
        if (pimPolicyPort == null) {
            return;
        }

        // Resolve policyId from cconfig: policyEngine.<action>
        String policyId = configString(config, "/policyEngine/" + action, null);

        if (policyId != null) {
            Map<String, Object> context = Map.of("policyId", policyId);
            PimPolicyPort.PolicyDecision decision = pimPolicyPort.evaluate(action, product, context);
            if (!decision.allowed()) {
                log.warn("Policy engine denied action '{}': {}", action, decision.reason());
                throw new IllegalArgumentException("Policy check failed: " + decision.reason());
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // INTERNAL: Config helpers
    // ═══════════════════════════════════════════════════════════════════════

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
