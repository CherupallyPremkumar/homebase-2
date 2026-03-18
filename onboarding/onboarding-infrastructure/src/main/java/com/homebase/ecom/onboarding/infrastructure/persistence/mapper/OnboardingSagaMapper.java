package com.homebase.ecom.onboarding.infrastructure.persistence.mapper;

import com.homebase.ecom.onboarding.model.OnboardingSaga;
import com.homebase.ecom.onboarding.model.OnboardingDocument;
import com.homebase.ecom.onboarding.infrastructure.persistence.entity.OnboardingSagaEntity;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Bidirectional mapper between OnboardingSaga domain model and OnboardingSagaEntity JPA entity.
 * Handles JSON serialization for nested collections (documents, training modules).
 */
public class OnboardingSagaMapper {

    private static final Logger log = LoggerFactory.getLogger(OnboardingSagaMapper.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OnboardingSaga toModel(OnboardingSagaEntity entity) {
        if (entity == null) return null;
        OnboardingSaga model = new OnboardingSaga();
        model.setId(entity.getId());
        model.setSupplierId(entity.getSupplierId());
        model.setApplicantName(entity.getApplicantName());
        model.setBusinessName(entity.getBusinessName());
        model.setBusinessType(entity.getBusinessType());
        model.setVerificationNotes(entity.getVerificationNotes());
        model.setTrainingProgress(entity.getTrainingProgress());
        model.setResubmissionCount(entity.getResubmissionCount());
        model.setSubmittedAt(entity.getSubmittedAt());
        model.setRejectionReason(entity.getRejectionReason());

        // Deserialize documents JSON
        model.setDocuments(deserializeDocuments(entity.getDocumentsJson()));

        // Deserialize training completed modules JSON
        model.setTrainingCompletedModules(deserializeStringList(entity.getTrainingCompletedModulesJson()));

        // STM state
        model.setCurrentState(entity.getCurrentState());
        model.setTenant(entity.tenant);
        return model;
    }

    public OnboardingSagaEntity toEntity(OnboardingSaga model) {
        if (model == null) return null;
        OnboardingSagaEntity entity = new OnboardingSagaEntity();
        entity.setId(model.getId());
        entity.setSupplierId(model.getSupplierId());
        entity.setApplicantName(model.getApplicantName());
        entity.setBusinessName(model.getBusinessName());
        entity.setBusinessType(model.getBusinessType());
        entity.setVerificationNotes(model.getVerificationNotes());
        entity.setTrainingProgress(model.getTrainingProgress());
        entity.setResubmissionCount(model.getResubmissionCount());
        entity.setSubmittedAt(model.getSubmittedAt());
        entity.setRejectionReason(model.getRejectionReason());

        // Serialize documents to JSON
        entity.setDocumentsJson(serializeDocuments(model.getDocuments()));

        // Serialize training completed modules to JSON
        entity.setTrainingCompletedModulesJson(serializeStringList(model.getTrainingCompletedModules()));

        // STM state
        entity.setCurrentState(model.getCurrentState());
        entity.tenant = model.getTenant();
        return entity;
    }

    // ── JSON helpers ──

    private List<OnboardingDocument> deserializeDocuments(String json) {
        if (json == null || json.isBlank()) return new ArrayList<>();
        try {
            return objectMapper.readValue(json, new TypeReference<List<OnboardingDocument>>() {});
        } catch (Exception e) {
            log.warn("Failed to deserialize documents JSON: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    private String serializeDocuments(List<OnboardingDocument> documents) {
        if (documents == null || documents.isEmpty()) return null;
        try {
            return objectMapper.writeValueAsString(documents);
        } catch (Exception e) {
            log.warn("Failed to serialize documents: {}", e.getMessage());
            return null;
        }
    }

    private List<String> deserializeStringList(String json) {
        if (json == null || json.isBlank()) return new ArrayList<>();
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            log.warn("Failed to deserialize string list JSON: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    private String serializeStringList(List<String> list) {
        if (list == null || list.isEmpty()) return null;
        try {
            return objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            log.warn("Failed to serialize string list: {}", e.getMessage());
            return null;
        }
    }
}
