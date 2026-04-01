package com.homebase.ecom.supplier.infrastructure.persistence.mapper;

import com.homebase.ecom.supplier.infrastructure.persistence.entity.SupplierEntity;
import com.homebase.ecom.supplier.model.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SupplierMapper bidirectional mapping.
 * Verifies all fields are correctly mapped between domain model and JPA entity.
 */
class SupplierMapperTest {

    private SupplierMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new SupplierMapper();
    }

    @Test
    void toModel_nullEntity_returnsNull() {
        assertNull(mapper.toModel(null));
    }

    @Test
    void toEntity_nullModel_returnsNull() {
        assertNull(mapper.toEntity((Supplier) null));
    }

    @Test
    void toModel_mapsAllFields() {
        SupplierEntity entity = createTestEntity();
        Supplier model = mapper.toModel(entity);

        assertNotNull(model);
        assertEquals("sup-001", model.getId());
        assertEquals("user-123", model.getUserId());
        assertEquals("Test Business", model.getBusinessName());
        assertEquals("COMPANY", model.getBusinessType());
        assertEquals("TAX-123", model.getTaxId());
        assertEquals("BANK-456", model.getBankAccountId());
        assertEquals("test@example.com", model.getContactEmail());
        assertEquals("9876543210", model.getContactPhone());
        assertEquals("123 Test St", model.getAddress());

        assertEquals(4.5, model.getRating(), 0.01);
        assertEquals(100, model.getTotalOrders());
        assertEquals(5, model.getTotalReturns());
        assertEquals(95.0, model.getFulfillmentRate(), 0.01);
        assertEquals(3.5, model.getAvgShippingDays(), 0.01);
        assertEquals(12.0, model.getCommissionRate(), 0.01);

        assertEquals("Policy violation", model.getSuspensionReason());
        assertEquals("Fraud", model.getTerminationReason());
        assertEquals("Low rating", model.getProbationReason());
        assertEquals("Incomplete docs", model.getRejectionReason());
        assertTrue(model.isProductsDisabled());
    }

    @Test
    void toEntity_mapsAllFields() {
        Supplier model = createTestModel();
        SupplierEntity entity = mapper.toEntity(model);

        assertNotNull(entity);
        assertEquals("sup-001", entity.getId());
        assertEquals("user-123", entity.getUserId());
        assertEquals("Test Business", entity.getBusinessName());
        assertEquals("COMPANY", entity.getBusinessType());
        assertEquals("TAX-123", entity.getTaxId());
        assertEquals("BANK-456", entity.getBankAccountId());
        assertEquals("test@example.com", entity.getContactEmail());
        assertEquals("9876543210", entity.getContactPhone());
        assertEquals("123 Test St", entity.getAddress());

        assertEquals(4.5, entity.getRating(), 0.01);
        assertEquals(100, entity.getTotalOrders());
        assertEquals(5, entity.getTotalReturns());
        assertEquals(95.0, entity.getFulfillmentRate(), 0.01);
        assertEquals(3.5, entity.getAvgShippingDays(), 0.01);
        assertEquals(12.0, entity.getCommissionRate(), 0.01);

        assertEquals("Policy violation", entity.getSuspensionReason());
        assertEquals("Fraud", entity.getTerminationReason());
        assertEquals("Low rating", entity.getProbationReason());
        assertTrue(entity.isProductsDisabled());
    }

    @Test
    void roundTrip_preservesAllFields() {
        Supplier original = createTestModel();
        original.addActivity("reviewSupplier", "Application reviewed");

        SupplierEntity entity = mapper.toEntity(original);
        Supplier roundTripped = mapper.toModel(entity);

        assertEquals(original.getId(), roundTripped.getId());
        assertEquals(original.getBusinessName(), roundTripped.getBusinessName());
        assertEquals(original.getBusinessType(), roundTripped.getBusinessType());
        assertEquals(original.getRating(), roundTripped.getRating(), 0.01);
        assertEquals(original.getTotalOrders(), roundTripped.getTotalOrders());
        assertEquals(original.getFulfillmentRate(), roundTripped.getFulfillmentRate(), 0.01);
        assertEquals(original.getCommissionRate(), roundTripped.getCommissionRate(), 0.01);
        assertEquals(original.isProductsDisabled(), roundTripped.isProductsDisabled());

        // Activities should round-trip
        assertEquals(1, roundTripped.obtainActivities().size());
    }

    @Test
    void toModel_handlesDefaultMetrics() {
        SupplierEntity entity = new SupplierEntity();
        entity.setId("sup-new");
        entity.setBusinessName("New Supplier");

        Supplier model = mapper.toModel(entity);

        assertEquals(0.0, model.getRating(), 0.01);
        assertEquals(0, model.getTotalOrders());
        assertEquals(0, model.getTotalReturns());
        assertEquals(0.0, model.getFulfillmentRate(), 0.01);
        assertEquals(0.0, model.getCommissionRate(), 0.01);
        assertFalse(model.isProductsDisabled());
    }

    // --- Test data helpers ---

    private SupplierEntity createTestEntity() {
        SupplierEntity entity = new SupplierEntity();
        entity.setId("sup-001");
        entity.setUserId("user-123");
        entity.setBusinessName("Test Business");
        entity.setBusinessType("COMPANY");
        entity.setTaxId("TAX-123");
        entity.setBankAccountId("BANK-456");
        entity.setContactEmail("test@example.com");
        entity.setContactPhone("9876543210");
        entity.setAddress("123 Test St");

        entity.setRating(4.5);
        entity.setTotalOrders(100);
        entity.setTotalReturns(5);
        entity.setFulfillmentRate(95.0);
        entity.setAvgShippingDays(3.5);
        entity.setCommissionRate(12.0);

        entity.setActiveDate(LocalDateTime.now());
        entity.setRejectionReason("Incomplete docs");
        entity.setSuspensionReason("Policy violation");
        entity.setTerminationReason("Fraud");
        entity.setProbationReason("Low rating");
        entity.setSuspendedDate(LocalDateTime.now());
        entity.setTerminatedDate(LocalDateTime.now());
        entity.setProbationDate(LocalDateTime.now());
        entity.setProductsDisabled(true);

        return entity;
    }

    private Supplier createTestModel() {
        Supplier model = new Supplier();
        model.setId("sup-001");
        model.setUserId("user-123");
        model.setBusinessName("Test Business");
        model.setBusinessType("COMPANY");
        model.setTaxId("TAX-123");
        model.setBankAccountId("BANK-456");
        model.setContactEmail("test@example.com");
        model.setContactPhone("9876543210");
        model.setAddress("123 Test St");

        model.setRating(4.5);
        model.setTotalOrders(100);
        model.setTotalReturns(5);
        model.setFulfillmentRate(95.0);
        model.setAvgShippingDays(3.5);
        model.setCommissionRate(12.0);

        model.setActiveDate(LocalDateTime.now());
        model.setRejectionReason("Incomplete docs");
        model.setSuspensionReason("Policy violation");
        model.setTerminationReason("Fraud");
        model.setProbationReason("Low rating");
        model.setSuspendedDate(LocalDateTime.now());
        model.setTerminatedDate(LocalDateTime.now());
        model.setProbationDate(LocalDateTime.now());
        model.setProductsDisabled(true);

        return model;
    }
}
