package com.homebase.ecom.onboarding.port;

import com.homebase.ecom.onboarding.model.OnboardingDocument;
import java.util.List;

/**
 * Port for document verification — external verification service integration.
 */
public interface DocumentVerificationPort {

    /**
     * Verify a list of documents. Returns true if all pass verification.
     */
    boolean verifyDocuments(List<OnboardingDocument> documents);

    /**
     * Check if all required document types are present.
     */
    boolean areRequiredDocumentsPresent(List<OnboardingDocument> documents, List<String> requiredTypes);
}
