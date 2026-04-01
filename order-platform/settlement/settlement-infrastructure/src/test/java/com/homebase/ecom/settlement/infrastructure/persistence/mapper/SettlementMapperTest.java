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
 * Money stores amount in smallest currency unit (paise for INR):
 * 10000 paise = Rs.100.00
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
        assertEquals(1000000L, entity.getOrderAmount().getAmount());
        assertEquals("INR", entity.getOrderAmount().getCurrency());
        assertEquals(Long.valueOf(150000L), entity.getCommissionAmountPaise());
        assertEquals(Long.valueOf(20000L), entity.getPlatformFeePaise());
        assertEquals(Long.valueOf(830000L), entity.getNetAmountPaise());
        assertEquals(LocalDate.of(2026, 3, 1), entity.getSettlementPeriodStart());
        assertEquals(LocalDate.of(2026, 3, 15), entity.getSettlementPeriodEnd());
        assertEquals("PAY-REF-001", entity.getDisbursementReference());
        assertEquals(1, entity.getAdjustments().size());
        assertEquals(new BigDecimal("-500"), entity.getAdjustments().get(0).getAmount());

        // Changeset 004 fields
        assertEquals("pay-001", entity.getPaymentId());
        assertEquals("SETT-2026-001", entity.getSettlementNumber());
        assertEquals("BANK-001", entity.getBankAccountId());
        assertEquals("NEFT", entity.getDisbursementMethod());
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
        assertEquals(1000000L, roundTripped.getOrderAmount().getAmount());
        assertEquals(150000L, roundTripped.getCommissionAmount().getAmount());
        assertEquals(20000L, roundTripped.getPlatformFee().getAmount());
        assertEquals(830000L, roundTripped.getNetAmount().getAmount());
        assertEquals(LocalDate.of(2026, 3, 1), roundTripped.getSettlementPeriodStart());
        assertEquals(LocalDate.of(2026, 3, 15), roundTripped.getSettlementPeriodEnd());
        assertEquals("PAY-REF-001", roundTripped.getDisbursementReference());
        assertEquals(1, roundTripped.getAdjustments().size());

        // Changeset 004 fields
        assertEquals("pay-001", roundTripped.getPaymentId());
        assertEquals("SETT-2026-001", roundTripped.getSettlementNumber());
        assertEquals("BANK-001", roundTripped.getBankAccountId());
        assertEquals("NEFT", roundTripped.getDisbursementMethod());
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

    @Test
    void mergeEntity_shouldPreserveExistingBaseFieldsAndUpdateBusiness() {
        Settlement original = createTestSettlement();
        SettlementEntity existing = mapper.toEntity(original);

        Settlement updatedModel = new Settlement();
        updatedModel.setId("settlement-001");
        updatedModel.setDescription("Updated settlement");
        updatedModel.setSupplierId("supplier-002");
        updatedModel.setOrderId("order-001");
        updatedModel.setOrderAmount(Money.of(2000000L, "INR"));
        updatedModel.setNetAmount(Money.of(1700000L, "INR"));
        updatedModel.setSettlementPeriodStart(LocalDate.of(2026, 4, 1));
        updatedModel.setSettlementPeriodEnd(LocalDate.of(2026, 4, 15));
        SettlementEntity updatedEntity = mapper.toEntity(updatedModel);

        mapper.mergeEntity(existing, updatedEntity);

        assertEquals("settlement-001", existing.getId());
        assertEquals("Updated settlement", existing.getDescription());
        assertEquals("supplier-002", existing.getSupplierId());
        assertEquals(2000000L, existing.getOrderAmount().getAmount());
    }

    /**
     * Money amounts are in smallest currency unit (paise for INR).
     * Rs.10,000 = 1,000,000 paise.
     */
    private Settlement createTestSettlement() {
        Settlement model = new Settlement();
        model.setId("settlement-001");
        model.setDescription("Test settlement");
        model.setSupplierId("supplier-001");
        model.setOrderId("order-001");
        model.setOrderAmount(Money.of(1000000L, "INR"));      // Rs.10,000
        model.setCommissionAmount(Money.of(150000L, "INR"));   // Rs.1,500
        model.setPlatformFee(Money.of(20000L, "INR"));         // Rs.200
        model.setNetAmount(Money.of(830000L, "INR"));          // Rs.8,300
        model.setCurrency("INR");
        model.setSettlementPeriodStart(LocalDate.of(2026, 3, 1));
        model.setSettlementPeriodEnd(LocalDate.of(2026, 3, 15));
        model.setDisbursementReference("PAY-REF-001");

        // Changeset 004 fields
        model.setPaymentId("pay-001");
        model.setSettlementNumber("SETT-2026-001");
        model.setBankAccountId("BANK-001");
        model.setDisbursementMethod("NEFT");

        SettlementAdjustment adjustment = new SettlementAdjustment(
                new BigDecimal("-500"), "Refund deduction", "SYSTEM");
        adjustment.setAdjustedAt(LocalDateTime.of(2026, 3, 10, 12, 0));
        model.getAdjustments().add(adjustment);

        return model;
    }
}
