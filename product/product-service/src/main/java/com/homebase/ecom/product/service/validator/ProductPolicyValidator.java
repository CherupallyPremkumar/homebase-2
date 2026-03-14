package com.homebase.ecom.product.service.validator;

import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.domain.port.PimPolicyPort;
import com.homebase.ecom.product.service.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Central component for enforcing PIM (Product Information Management) policies.
 * Validates product fields at each lifecycle transition:
 * - submitForReview: name, description, category required
 * - approve/publish: name, description, category, at least one image required
 * - reject: comment required
 */
@Component
public class ProductPolicyValidator {

    private static final Logger log = LoggerFactory.getLogger(ProductPolicyValidator.class);

    @Autowired(required = false)
    private PimPolicyPort pimPolicyPort;

    /**
     * POLICY: Lifecycle -- Submit for Review.
     * Validates that name, description, and category are present.
     */
    public void validateSubmitForReview(Product product) {
        // Direct field validation: name is required
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            log.warn("Submit blocked: product name is missing");
            throw new NameRequiredForSubmitException();
        }

        // Direct field validation: description is required
        if (product.getDescription() == null || product.getDescription().trim().isEmpty()) {
            log.warn("Submit blocked: product description is missing");
            throw new DescriptionRequiredForSubmitException();
        }

        // Direct field validation: category is required
        if (product.getCategoryId() == null || product.getCategoryId().trim().isEmpty()) {
            log.warn("Submit blocked: product category is missing");
            throw new CategoryRequiredForSubmitException();
        }

        // Delegate to external policy engine if available (for advanced rules)
        if (pimPolicyPort != null) {
            PimPolicyPort.PolicyDecision decision = pimPolicyPort.evaluate("pim-submit-review", product, null);
            if (!decision.allowed()) {
                log.warn("Submission blocked by policy: {}", decision.reason());
                if (decision.reason() != null && decision.reason().contains("Category")) {
                    throw new CategoryRequiredForSubmitException();
                }
                throw new IllegalArgumentException("Submission failed: " + decision.reason());
            }
        }
    }

    /**
     * POLICY: Lifecycle -- Publish (Approve).
     * When the external policy engine is available, delegates to it for full validation
     * (description, images, pricing, etc.). Otherwise falls back to direct field checks.
     */
    public void validatePublish(Product product) {
        String productId = product.getId() != null ? product.getId().toString() : product.getName();

        // Delegate to external policy engine if available
        if (pimPolicyPort != null) {
            PimPolicyPort.PolicyDecision decision = pimPolicyPort.evaluate("pim-publish-approved", product, null);
            if (!decision.allowed()) {
                log.warn("Publication blocked by policy: {}", decision.reason());
                if (decision.reason() != null && decision.reason().contains("Image")) {
                    throw new ImageRequiredForPublishException(productId);
                }
                if (decision.reason() != null && decision.reason().contains("Description")) {
                    throw new DescriptionRequiredForPublishException(productId);
                }
                throw new IllegalArgumentException("Approval failed: " + decision.reason());
            }
        } else {
            // Fallback: direct field validation when policy engine is not available
            if (product.getDescription() == null || product.getDescription().trim().isEmpty()) {
                log.warn("Publish blocked: product description is missing for {}", productId);
                throw new DescriptionRequiredForPublishException(productId);
            }
            if (product.getMedia() == null || product.getMedia().isEmpty()) {
                log.warn("Publish blocked: no images for product {}", productId);
                throw new ImageRequiredForPublishException(productId);
            }
        }
    }

    /**
     * RULE: Quality -- auto-reject threshold score.
     * Products with a quality score at or below this value are flagged for auto-rejection.
     */
    public int getAutoRejectBelowScore() {
        return 30;
    }

    /**
     * POLICY: Lifecycle -- Rejection comment.
     * A comment is mandatory when rejecting a product to provide supplier feedback.
     */
    public void validateRejectionComment(String comment) {
        if (comment == null || comment.trim().isEmpty()) {
            throw new ProductRejectionCommentRequiredException();
        }
    }
}
