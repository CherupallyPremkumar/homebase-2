package com.homebase.ecom.onboarding.mapper;

import com.homebase.ecom.onboarding.infrastructure.persistence.entity.OnboardingSagaEntity;
import com.homebase.ecom.onboarding.infrastructure.persistence.mapper.OnboardingSagaMapper;
import com.homebase.ecom.onboarding.model.OnboardingDocument;
import com.homebase.ecom.onboarding.model.OnboardingSaga;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for OnboardingSagaMapper — bidirectional mapping
 * between domain model and JPA entity, including JSON serialization.
 */
class OnboardingSagaMapperTest {

    private OnboardingSagaMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new OnboardingSagaMapper();
    }

    @Test
    void toModel_null_returnsNull() {
        assertNull(mapper.toModel(null));
    }

    @Test
    void toEntity_null_returnsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toModel_basicFields_mapped() {
        OnboardingSagaEntity entity = new OnboardingSagaEntity();
        entity.setId("onb-001");
        entity.setSupplierId("sup-001");
        entity.setApplicantName("Rajesh");
        entity.setBusinessName("Silk Weaves");
        entity.setBusinessType("MANUFACTURER");
        entity.setVerificationNotes("All good");
        entity.setTrainingProgress(66);
        entity.setResubmissionCount(1);
        entity.setRejectionReason("Test reason");
        LocalDateTime now = LocalDateTime.now();
        entity.setSubmittedAt(now);

        OnboardingSaga model = mapper.toModel(entity);

        assertEquals("onb-001", model.getId());
        assertEquals("sup-001", model.getSupplierId());
        assertEquals("Rajesh", model.getApplicantName());
        assertEquals("Silk Weaves", model.getBusinessName());
        assertEquals("MANUFACTURER", model.getBusinessType());
        assertEquals("All good", model.getVerificationNotes());
        assertEquals(66, model.getTrainingProgress());
        assertEquals(1, model.getResubmissionCount());
        assertEquals("Test reason", model.getRejectionReason());
        assertEquals(now, model.getSubmittedAt());
    }

    @Test
    void toEntity_basicFields_mapped() {
        OnboardingSaga model = new OnboardingSaga();
        model.setId("onb-002");
        model.setSupplierId("sup-002");
        model.setApplicantName("Priya");
        model.setBusinessName("Artisan Crafts");
        model.setBusinessType("ARTISAN");
        model.setTrainingProgress(100);
        model.setResubmissionCount(0);

        OnboardingSagaEntity entity = mapper.toEntity(model);

        assertEquals("onb-002", entity.getId());
        assertEquals("sup-002", entity.getSupplierId());
        assertEquals("Priya", entity.getApplicantName());
        assertEquals("Artisan Crafts", entity.getBusinessName());
        assertEquals("ARTISAN", entity.getBusinessType());
        assertEquals(100, entity.getTrainingProgress());
        assertEquals(0, entity.getResubmissionCount());
    }

    @Test
    void toModel_documentsJson_deserialized() {
        OnboardingSagaEntity entity = new OnboardingSagaEntity();
        entity.setId("onb-003");
        entity.setDocumentsJson("[{\"type\":\"TAX_ID\",\"fileUrl\":\"https://example.com/tax.pdf\",\"status\":\"VERIFIED\"},{\"type\":\"BANK_PROOF\",\"fileUrl\":\"https://example.com/bank.pdf\",\"status\":\"PENDING\"}]");

        OnboardingSaga model = mapper.toModel(entity);

        assertNotNull(model.getDocuments());
        assertEquals(2, model.getDocuments().size());
        assertEquals("TAX_ID", model.getDocuments().get(0).getType());
        assertEquals("VERIFIED", model.getDocuments().get(0).getStatus());
        assertEquals("BANK_PROOF", model.getDocuments().get(1).getType());
        assertEquals("PENDING", model.getDocuments().get(1).getStatus());
    }

    @Test
    void toEntity_documents_serialized() {
        OnboardingSaga model = new OnboardingSaga();
        model.setId("onb-004");
        OnboardingDocument doc1 = new OnboardingDocument("TAX_ID", "https://example.com/tax.pdf");
        doc1.setStatus("VERIFIED");
        OnboardingDocument doc2 = new OnboardingDocument("BUSINESS_LICENSE", "https://example.com/license.pdf");
        model.setDocuments(List.of(doc1, doc2));

        OnboardingSagaEntity entity = mapper.toEntity(model);

        assertNotNull(entity.getDocumentsJson());
        assertTrue(entity.getDocumentsJson().contains("TAX_ID"));
        assertTrue(entity.getDocumentsJson().contains("BUSINESS_LICENSE"));
    }

    @Test
    void toModel_trainingModulesJson_deserialized() {
        OnboardingSagaEntity entity = new OnboardingSagaEntity();
        entity.setId("onb-005");
        entity.setTrainingCompletedModulesJson("[\"PLATFORM_BASICS\",\"SELLER_POLICIES\"]");

        OnboardingSaga model = mapper.toModel(entity);

        assertNotNull(model.getTrainingCompletedModules());
        assertEquals(2, model.getTrainingCompletedModules().size());
        assertTrue(model.getTrainingCompletedModules().contains("PLATFORM_BASICS"));
        assertTrue(model.getTrainingCompletedModules().contains("SELLER_POLICIES"));
    }

    @Test
    void toEntity_trainingModules_serialized() {
        OnboardingSaga model = new OnboardingSaga();
        model.setId("onb-006");
        model.setTrainingCompletedModules(List.of("PLATFORM_BASICS", "SELLER_POLICIES", "SHIPPING_GUIDELINES"));

        OnboardingSagaEntity entity = mapper.toEntity(model);

        assertNotNull(entity.getTrainingCompletedModulesJson());
        assertTrue(entity.getTrainingCompletedModulesJson().contains("PLATFORM_BASICS"));
        assertTrue(entity.getTrainingCompletedModulesJson().contains("SHIPPING_GUIDELINES"));
    }

    @Test
    void roundTrip_modelToEntityToModel_preservesAllFields() {
        OnboardingSaga original = new OnboardingSaga();
        original.setId("onb-rt");
        original.setSupplierId("sup-rt");
        original.setApplicantName("Round Trip");
        original.setBusinessName("Test Corp");
        original.setBusinessType("DISTRIBUTOR");
        original.setVerificationNotes("Verified");
        original.setTrainingProgress(33);
        original.setResubmissionCount(2);
        original.setRejectionReason(null);
        original.setSubmittedAt(LocalDateTime.of(2026, 1, 15, 10, 0));
        original.setDocuments(List.of(
                new OnboardingDocument("TAX_ID", "https://example.com/tax.pdf"),
                new OnboardingDocument("BANK_PROOF", "https://example.com/bank.pdf")
        ));
        original.setTrainingCompletedModules(List.of("PLATFORM_BASICS"));

        OnboardingSagaEntity entity = mapper.toEntity(original);
        OnboardingSaga restored = mapper.toModel(entity);

        assertEquals(original.getId(), restored.getId());
        assertEquals(original.getSupplierId(), restored.getSupplierId());
        assertEquals(original.getApplicantName(), restored.getApplicantName());
        assertEquals(original.getBusinessName(), restored.getBusinessName());
        assertEquals(original.getBusinessType(), restored.getBusinessType());
        assertEquals(original.getVerificationNotes(), restored.getVerificationNotes());
        assertEquals(original.getTrainingProgress(), restored.getTrainingProgress());
        assertEquals(original.getResubmissionCount(), restored.getResubmissionCount());
        assertEquals(original.getSubmittedAt(), restored.getSubmittedAt());
        assertEquals(original.getDocuments().size(), restored.getDocuments().size());
        assertEquals(original.getTrainingCompletedModules().size(), restored.getTrainingCompletedModules().size());
    }

    @Test
    void toModel_emptyJsonFields_returnsEmptyLists() {
        OnboardingSagaEntity entity = new OnboardingSagaEntity();
        entity.setId("onb-empty");
        entity.setDocumentsJson(null);
        entity.setTrainingCompletedModulesJson("");

        OnboardingSaga model = mapper.toModel(entity);

        assertNotNull(model.getDocuments());
        assertTrue(model.getDocuments().isEmpty());
        assertNotNull(model.getTrainingCompletedModules());
        assertTrue(model.getTrainingCompletedModules().isEmpty());
    }

    @Test
    void toModel_invalidJson_returnsEmptyLists() {
        OnboardingSagaEntity entity = new OnboardingSagaEntity();
        entity.setId("onb-invalid");
        entity.setDocumentsJson("not valid json");
        entity.setTrainingCompletedModulesJson("{bad}");

        OnboardingSaga model = mapper.toModel(entity);

        assertNotNull(model.getDocuments());
        assertTrue(model.getDocuments().isEmpty());
        assertNotNull(model.getTrainingCompletedModules());
        assertTrue(model.getTrainingCompletedModules().isEmpty());
    }
}
