package com.homebase.ecom.settlement.infrastructure.persistence.mapper;

import com.homebase.ecom.settlement.model.Settlement;
import com.homebase.ecom.settlement.model.SettlementAdjustment;
import com.homebase.ecom.settlement.infrastructure.persistence.entity.SettlementEntity;
import com.homebase.ecom.shared.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Bidirectional mapping tests for SettlementMapper.
 */
class SettlementMapperTest {

    private SettlementMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new SettlementMapper();
    }

    @Test
    void toEntity_shouldMapAllFields() {
        Settlement model = createTestSettlement();

        SettlementEntity entity = mapper.toEntity(model);

        assertNotNull(entity);
        assertEquals("settlement-001", entity.getId());
        assertEquals("Test settlement", entity.getDescription());
        assertEquals("supplier-001", entity.getSupplierId());
        assertEquals("order-001", entity.getOrderId());
        assertEquals(new BigDecimal("10000"), entity.getOrderAmount().getAmount());
        assertEquals("INR", entity.getOrderAmount().getCurrency());
        assertEquals(new BigDecimal("1500"), entity.getCommissionAmount().getAmount());
        assertEquals(new BigDecimal("200"), entity.getPlatformFee().getAmount());
        assertEquals(new BigDecimal("8300"), entity.getNetAmount().getAmount());
        assertEquals(LocalDate.of(2026, 3, 1), entity.getSettlementPeriodStart());
        assertEquals(LocalDate.of(2026, 3, 15), entity.getSettlementPeriodEnd());
        assertEquals("PAY-REF-001", entity.getDisbursementReference());
        assertEquals(1, entity.getAdjustments().size());
        assertEquals(new BigDecimal("-500"), entity.getAdjustments().get(0).getAmount());
    }

    @Test
    void toModel_shouldMapAllFields() {
        Settlement original = createTestSettlement();
        SettlementEntity entity = mapper.toEntity(original);

        Settlement roundTripped = mapper.toModel(entity);

        assertNotNull(roundTripped);
        assertEquals("settlement-001", roundTripped.getId());
        assertEquals("Test settlement", roundTripped.getDescription());
        assertEquals("supplier-001", roundTripped.getSupplierId());
        assertEquals("order-001", roundTripped.getOrderId());
        assertEquals(new BigDecimal("10000"), roundTripped.getOrderAmount().getAmount());
        assertEquals(new BigDecimal("1500"), roundTripped.getCommissionAmount().getAmount());
        assertEquals(new BigDecimal("200"), roundTripped.getPlatformFee().getAmount());
        assertEquals(new BigDecimal("8300"), roundTripped.getNetAmount().getAmount());
        assertEquals(LocalDate.of(2026, 3, 1), roundTripped.getSettlementPeriodStart());
        assertEquals(LocalDate.of(2026, 3, 15), roundTripped.getSettlementPeriodEnd());
        assertEquals("PAY-REF-001", roundTripped.getDisbursementReference());
        assertEquals(1, roundTripped.getAdjustments().size());
    }

    @Test
    void toEntity_nullInput_shouldReturnNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toModel_nullInput_shouldReturnNull() {
        assertNull(mapper.toModel(null));
    }

    @Test
    void roundTrip_withActivities() {
        Settlement model = createTestSettlement();
        model.addActivity("verifyBankDetails", "Bank details verified");
        model.addActivity("auditTrailCheck", "Audit trail complete");

        SettlementEntity entity = mapper.toEntity(model);
        Settlement roundTripped = mapper.toModel(entity);

        assertEquals(2, roundTripped.obtainActivities().size());
    }

    @Test
    void roundTrip_emptyAdjustments() {
        Settlement model = new Settlement();
        model.setId("settlement-empty");
        model.setSupplierId("sup-x");

        SettlementEntity entity = mapper.toEntity(model);
        Settlement roundTripped = mapper.toModel(entity);

        assertNotNull(roundTripped.getAdjustments());
        assertTrue(roundTripped.getAdjustments().isEmpty());
    }

    private Settlement createTestSettlement() {
        Settlement model = new Settlement();
        model.setId("settlement-001");
        model.setDescription("Test settlement");
        model.setSupplierId("supplier-001");
        model.setOrderId("order-001");
        model.setOrderAmount(new Money(new BigDecimal("10000"), "INR"));
        model.setCommissionAmount(new Money(new BigDecimal("1500"), "INR"));
        model.setPlatformFee(new Money(new BigDecimal("200"), "INR"));
        model.setNetAmount(new Money(new BigDecimal("8300"), "INR"));
        model.setCurrency("INR");
        model.setSettlementPeriodStart(LocalDate.of(2026, 3, 1));
        model.setSettlementPeriodEnd(LocalDate.of(2026, 3, 15));
        model.setDisbursementReference("PAY-REF-001");

        SettlementAdjustment adjustment = new SettlementAdjustment(
                new BigDecimal("-500"), "Refund deduction", "SYSTEM");
        adjustment.setAdjustedAt(LocalDateTime.of(2026, 3, 10, 12, 0));
        model.getAdjustments().add(adjustment);

        return model;
    }
}
