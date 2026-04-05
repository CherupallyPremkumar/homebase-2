package com.homebase.ecom.onboarding.infrastructure.adapter;

import com.homebase.ecom.onboarding.model.OnboardingDocument;
import com.homebase.ecom.onboarding.port.DocumentVerificationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Default adapter for document verification.
 * In production, this would integrate with an external document verification service.
 */
public class DocumentVerificationAdapter implements DocumentVerificationPort {

    private static final Logger log = LoggerFactory.getLogger(DocumentVerificationAdapter.class);

    @Override
    public boolean verifyDocuments(List<OnboardingDocument> documents) {
        if (documents == null || documents.isEmpty()) {
            return false;
        }
        // Default: all documents with valid URLs are considered verifiable
        return documents.stream()
                .allMatch(doc -> doc.getFileUrl() != null && !doc.getFileUrl().isBlank());
    }

    @Override
    public boolean areRequiredDocumentsPresent(List<OnboardingDocument> documents, List<String> requiredTypes) {
        if (documents == null || requiredTypes == null) return false;
        Set<String> submittedTypes = documents.stream()
                .map(OnboardingDocument::getType)
                .collect(Collectors.toSet());
        boolean allPresent = submittedTypes.containsAll(requiredTypes);
        if (!allPresent) {
            log.warn("Missing required documents. Required: {}, Submitted: {}", requiredTypes, submittedTypes);
        }
        return allPresent;
    }
}
