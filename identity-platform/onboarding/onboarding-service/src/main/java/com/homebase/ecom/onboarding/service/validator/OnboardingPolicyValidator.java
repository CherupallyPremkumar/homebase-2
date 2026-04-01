package com.homebase.ecom.onboarding.service.validator;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.onboarding.model.OnboardingSaga;
import com.homebase.ecom.onboarding.model.OnboardingDocument;
import com.homebase.ecom.cconfig.sdk.CconfigClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Central policy validator for onboarding rules.
 * Reads configurable rules from cconfig (org/chenile/config/onboarding.json).
 *
 * Validates:
 * - Document completeness (all required document types present)
 * - Resubmission limits (maxResubmissions)
 * - Timeout (autoRejectAfterDays)
 */
public class OnboardingPolicyValidator {

    private static final Logger log = LoggerFactory.getLogger(OnboardingPolicyValidator.class);

    @Autowired(required = false)
    private CconfigClient cconfigClient;

    private final ObjectMapper mapper = new ObjectMapper();

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Document Completeness
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Validates that all required document types have been submitted.
     * Required types come from cconfig: policies.documents.requiredDocuments.
     */
    public void validateDocumentCompleteness(OnboardingSaga saga) {
        List<String> requiredDocs = getRequiredDocuments();
        if (requiredDocs.isEmpty()) return;

        List<OnboardingDocument> documents = saga.getDocuments();
        if (documents == null || documents.isEmpty()) {
            throw new IllegalArgumentException("No documents submitted. Required: " + requiredDocs);
        }

        Set<String> submittedTypes = documents.stream()
                .map(OnboardingDocument::getType)
                .collect(Collectors.toSet());

        List<String> missing = requiredDocs.stream()
                .filter(type -> !submittedTypes.contains(type))
                .collect(Collectors.toList());

        if (!missing.isEmpty()) {
            throw new IllegalArgumentException("Missing required documents: " + missing);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Resubmission Limits
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Validates that the seller has not exceeded the maximum number of resubmissions.
     */
    public void validateResubmissionLimit(OnboardingSaga saga) {
        int maxResubmissions = getMaxResubmissions();
        if (saga.getResubmissionCount() >= maxResubmissions) {
            throw new IllegalArgumentException(
                    "Maximum resubmissions (" + maxResubmissions + ") exceeded. Application must be re-created.");
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Timeout
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Checks if the application has timed out based on autoRejectAfterDays.
     * Returns true if the application should be auto-rejected.
     */
    public boolean isTimedOut(OnboardingSaga saga) {
        int autoRejectDays = getAutoRejectAfterDays();
        return saga.getDaysSinceSubmission() >= autoRejectDays;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // CONFIG ACCESSORS
    // ═══════════════════════════════════════════════════════════════════════

    public int getDocumentVerificationDays() {
        return configInt("/policies/verification/documentVerificationDays", 5);
    }

    public int getBusinessVerificationDays() {
        return configInt("/policies/verification/businessVerificationDays", 10);
    }

    public int getTrainingModulesRequired() {
        return configInt("/policies/training/trainingModulesRequired", 3);
    }

    public int getMaxResubmissions() {
        return configInt("/policies/resubmission/maxResubmissions", 3);
    }

    public int getAutoRejectAfterDays() {
        return configInt("/policies/timeout/autoRejectAfterDays", 30);
    }

    public List<String> getRequiredDocuments() {
        JsonNode config = getOnboardingConfig();
        JsonNode docsNode = config.at("/policies/documents/requiredDocuments");
        if (!docsNode.isMissingNode() && docsNode.isArray()) {
            List<String> result = new ArrayList<>();
            for (JsonNode item : docsNode) {
                result.add(item.asText());
            }
            return result;
        }
        return List.of("TAX_ID", "BUSINESS_LICENSE", "BANK_PROOF");
    }

    // ═══════════════════════════════════════════════════════════════════════
    // INTERNAL: Config helpers
    // ═══════════════════════════════════════════════════════════════════════

    private JsonNode getOnboardingConfig() {
        try {
            if (cconfigClient != null) {
                Map<String, Object> map = cconfigClient.value("onboarding", null);
                if (map != null) {
                    return mapper.valueToTree(map);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to load onboarding.json from cconfig, using defaults: {}", e.getMessage());
        }
        return mapper.createObjectNode();
    }

    private int configInt(String path, int defaultVal) {
        JsonNode config = getOnboardingConfig();
        JsonNode node = config.at(path);
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : defaultVal;
    }
}
