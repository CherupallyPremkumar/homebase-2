package com.homebase.ecom.payment.infrastructure.persistence.mapper;

import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.domain.model.PaymentActivityLogEntry;
import com.homebase.ecom.payment.infrastructure.persistence.entity.PaymentActivityLogEntity;
import com.homebase.ecom.payment.infrastructure.persistence.entity.PaymentEntity;
import org.chenile.stm.State;
import org.chenile.workflow.activities.model.ActivityLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaymentMapperTest {

    private PaymentMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PaymentMapper();
    }

    // ── Entity -> Model tests ──────────────────────────────────────────────

    @Test
    void toModel_nullEntity_returnsNull() {
        assertNull(mapper.toModel(null));
    }

    @Test
    void toModel_fullEntity_mapsAllCoreFields() {
        PaymentEntity entity = buildFullEntity();
        Payment model = mapper.toModel(entity);

        assertEquals("pay-1", model.getId());
        assertEquals("ORD-001", model.getOrderId());
        assertEquals("CUST-001", model.getCustomerId());
        assertEquals(new BigDecimal("2500.00"), model.getAmount());
        assertEquals("INR", model.getCurrency());
        assertEquals("UPI", model.getPaymentMethod());
        assertEquals("gw-txn-001", model.getGatewayTransactionId());
        assertEquals("{\"status\":\"captured\"}", model.getGatewayResponse());
        assertEquals(2, model.getRetryCount());
        assertEquals("Insufficient funds", model.getFailureReason());
        assertEquals(new State("SUCCEEDED", "payment-flow"), model.getCurrentState());
    }

    @Test
    void toModel_mapsAllNewFields() {
        PaymentEntity entity = buildFullEntity();
        Payment model = mapper.toModel(entity);

        assertEquals("CREDIT_CARD", model.getPaymentMethodType());
        assertEquals("4242", model.getCardLastFour());
        assertEquals("VISA", model.getCardBrand());
        assertEquals("priya@okicici", model.getUpiId());
        assertEquals("RAZORPAY", model.getGatewayName());
        assertEquals("order_RPay_001", model.getGatewayOrderId());
        assertEquals("pay_RPay_001", model.getGatewayPaymentId());
        assertEquals("chk-001", model.getCheckoutId());
        assertEquals("idem-001", model.getIdempotencyKey());
        assertEquals(new BigDecimal("500.00"), model.getRefundAmount());
        assertEquals("Product defective", model.getRefundReason());
    }

    @Test
    void toModel_mapsActivities() {
        PaymentEntity entity = new PaymentEntity();
        entity.setId("pay-1");
        entity.setAmount(BigDecimal.ZERO);

        PaymentActivityLogEntity log = new PaymentActivityLogEntity();
        log.setEventId("initiate");
        log.setComment("Payment initiated");
        log.setSuccess(true);
        entity.setActivities(List.of(log));

        Payment model = mapper.toModel(entity);

        assertEquals(1, model.getActivities().size());
        ActivityLog al = model.getActivities().get(0);
        assertEquals("initiate", al.getName());
        assertEquals("Payment initiated", al.getComment());
        assertTrue(al.getSuccess());
    }

    @Test
    void toModel_nullCollections_noExceptions() {
        PaymentEntity entity = new PaymentEntity();
        entity.setId("pay-1");
        entity.setAmount(BigDecimal.ZERO);
        entity.setActivities(null);

        Payment model = mapper.toModel(entity);

        assertNotNull(model);
        assertEquals("pay-1", model.getId());
    }

    @Test
    void toModel_stateFlowFieldsMapped() {
        PaymentEntity entity = new PaymentEntity();
        entity.setId("pay-1");
        entity.setAmount(BigDecimal.ZERO);
        State state = new State("PROCESSING", "payment-flow");
        entity.setCurrentState(state);

        Payment model = mapper.toModel(entity);

        assertNotNull(model.getCurrentState());
        assertEquals("PROCESSING", model.getCurrentState().getStateId());
        assertEquals("payment-flow", model.getCurrentState().getFlowId());
    }

    @Test
    void toModel_nullCurrentState_preservedAsNull() {
        PaymentEntity entity = new PaymentEntity();
        entity.setId("pay-1");
        entity.setAmount(BigDecimal.ZERO);
        entity.setCurrentState(null);

        Payment model = mapper.toModel(entity);

        assertNull(model.getCurrentState());
    }

    @Test
    void toModel_createdAndModifiedTimes_mapped() {
        PaymentEntity entity = new PaymentEntity();
        entity.setId("pay-1");
        entity.setAmount(BigDecimal.ZERO);
        Date created = new Date(1000000L);
        Date modified = new Date(2000000L);
        entity.setCreatedTime(created);
        entity.setLastModifiedTime(modified);

        Payment model = mapper.toModel(entity);

        assertEquals(created, model.getCreatedTime());
        assertEquals(modified, model.getLastModifiedTime());
    }

    @Test
    void toModel_zeroRetryCount_preserved() {
        PaymentEntity entity = new PaymentEntity();
        entity.setId("pay-1");
        entity.setAmount(BigDecimal.ZERO);
        entity.setRetryCount(0);

        Payment model = mapper.toModel(entity);

        assertEquals(0, model.getRetryCount());
    }

    @Test
    void toModel_nullableFieldsPreservedAsNull() {
        PaymentEntity entity = new PaymentEntity();
        entity.setId("pay-1");
        entity.setAmount(BigDecimal.ZERO);
        // All nullable fields left null

        Payment model = mapper.toModel(entity);

        assertNull(model.getPaymentMethodType());
        assertNull(model.getCardLastFour());
        assertNull(model.getCardBrand());
        assertNull(model.getUpiId());
        assertNull(model.getGatewayName());
        assertNull(model.getGatewayOrderId());
        assertNull(model.getGatewayPaymentId());
        assertNull(model.getCheckoutId());
        assertNull(model.getIdempotencyKey());
        assertNull(model.getRefundReason());
    }

    // ── Model -> Entity tests ──────────────────────────────────────────────

    @Test
    void toEntity_nullModel_returnsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toEntity_fullModel_mapsAllCoreFields() {
        Payment model = buildFullModel();
        PaymentEntity entity = mapper.toEntity(model);

        assertEquals("pay-1", entity.getId());
        assertEquals("ORD-001", entity.getOrderId());
        assertEquals("CUST-001", entity.getCustomerId());
        assertEquals(new BigDecimal("2500.00"), entity.getAmount());
        assertEquals("INR", entity.getCurrency());
        assertEquals("UPI", entity.getPaymentMethod());
        assertEquals("gw-txn-001", entity.getGatewayTransactionId());
        assertEquals("{\"status\":\"captured\"}", entity.getGatewayResponse());
        assertEquals(2, entity.getRetryCount());
        assertEquals("Insufficient funds", entity.getFailureReason());
        assertEquals(new State("SUCCEEDED", "payment-flow"), entity.getCurrentState());
    }

    @Test
    void toEntity_mapsAllNewFields() {
        Payment model = buildFullModel();
        PaymentEntity entity = mapper.toEntity(model);

        assertEquals("CREDIT_CARD", entity.getPaymentMethodType());
        assertEquals("4242", entity.getCardLastFour());
        assertEquals("VISA", entity.getCardBrand());
        assertEquals("priya@okicici", entity.getUpiId());
        assertEquals("RAZORPAY", entity.getGatewayName());
        assertEquals("order_RPay_001", entity.getGatewayOrderId());
        assertEquals("pay_RPay_001", entity.getGatewayPaymentId());
        assertEquals("chk-001", entity.getCheckoutId());
        assertEquals("idem-001", entity.getIdempotencyKey());
        assertEquals(new BigDecimal("500.00"), entity.getRefundAmount());
        assertEquals("Product defective", entity.getRefundReason());
    }

    @Test
    void toEntity_mapsActivities() {
        Payment model = new Payment();
        model.setId("pay-1");
        model.setAmount(BigDecimal.ZERO);

        PaymentActivityLogEntry log = new PaymentActivityLogEntry();
        log.activityName = "process";
        log.activityComment = "Processing payment";
        log.activitySuccess = true;
        model.setActivities(List.of(log));

        PaymentEntity entity = mapper.toEntity(model);

        assertEquals(1, entity.getActivities().size());
        PaymentActivityLogEntity ale = entity.getActivities().get(0);
        assertEquals("process", ale.getName());
        assertEquals("Processing payment", ale.getComment());
        assertTrue(ale.getSuccess());
    }

    @Test
    void toEntity_nullCollections_noExceptions() {
        Payment model = new Payment();
        model.setId("pay-1");
        model.setActivities(null);

        PaymentEntity entity = mapper.toEntity(model);

        assertNotNull(entity);
        assertEquals("pay-1", entity.getId());
    }

    @Test
    void toEntity_stateFlowFieldsMapped() {
        Payment model = new Payment();
        model.setId("pay-1");
        State state = new State("FAILED", "payment-flow");
        model.setCurrentState(state);

        PaymentEntity entity = mapper.toEntity(model);

        assertNotNull(entity.getCurrentState());
        assertEquals("FAILED", entity.getCurrentState().getStateId());
        assertEquals("payment-flow", entity.getCurrentState().getFlowId());
    }

    // ── mergeEntity tests ───────────────────────────────────────────────────

    @Test
    void mergeEntity_nullIncoming_returnsExisting() {
        PaymentEntity existing = buildFullEntity();
        PaymentEntity result = mapper.mergeEntity(null, existing);
        assertSame(existing, result);
    }

    @Test
    void mergeEntity_nullExisting_returnsNewEntity() {
        Payment incoming = buildFullModel();
        PaymentEntity result = mapper.mergeEntity(incoming, null);
        assertNotNull(result);
        assertEquals("pay-1", result.getId());
    }

    @Test
    void mergeEntity_updatesOnlyProvidedFields() {
        PaymentEntity existing = buildFullEntity();
        Payment incoming = new Payment();
        incoming.setGatewayTransactionId("new-txn-id");
        incoming.setCurrentState(new State("SETTLED", "payment-flow"));
        incoming.setAmount(new BigDecimal("2500.00")); // explicitly set to preserve

        PaymentEntity result = mapper.mergeEntity(incoming, existing);

        assertEquals("new-txn-id", result.getGatewayTransactionId());
        assertEquals("SETTLED", result.getCurrentState().getStateId());
        // Existing fields preserved when not null in incoming
        assertEquals("ORD-001", result.getOrderId());
        assertEquals("CUST-001", result.getCustomerId());
        assertEquals(new BigDecimal("2500.00"), result.getAmount());
    }

    @Test
    void mergeEntity_clearableFields() {
        PaymentEntity existing = buildFullEntity();
        Payment incoming = new Payment();
        incoming.setFailureReason(null); // explicitly cleared
        incoming.setCurrentState(existing.getCurrentState());

        PaymentEntity result = mapper.mergeEntity(incoming, existing);

        assertNull(result.getFailureReason());
    }

    // ── Round-trip tests ───────────────────────────────────────────────────

    @Test
    void roundTrip_entityToModelToEntity_preservesCoreFields() {
        PaymentEntity original = buildFullEntity();
        Payment model = mapper.toModel(original);
        PaymentEntity roundTripped = mapper.toEntity(model);

        assertEquals(original.getId(), roundTripped.getId());
        assertEquals(original.getOrderId(), roundTripped.getOrderId());
        assertEquals(original.getCustomerId(), roundTripped.getCustomerId());
        assertEquals(original.getAmount(), roundTripped.getAmount());
        assertEquals(original.getCurrency(), roundTripped.getCurrency());
        assertEquals(original.getPaymentMethod(), roundTripped.getPaymentMethod());
        assertEquals(original.getGatewayTransactionId(), roundTripped.getGatewayTransactionId());
        assertEquals(original.getGatewayResponse(), roundTripped.getGatewayResponse());
        assertEquals(original.getRetryCount(), roundTripped.getRetryCount());
        assertEquals(original.getFailureReason(), roundTripped.getFailureReason());
        assertEquals(original.getCurrentState(), roundTripped.getCurrentState());
        // New fields
        assertEquals(original.getPaymentMethodType(), roundTripped.getPaymentMethodType());
        assertEquals(original.getCardLastFour(), roundTripped.getCardLastFour());
        assertEquals(original.getCardBrand(), roundTripped.getCardBrand());
        assertEquals(original.getUpiId(), roundTripped.getUpiId());
        assertEquals(original.getGatewayName(), roundTripped.getGatewayName());
        assertEquals(original.getGatewayOrderId(), roundTripped.getGatewayOrderId());
        assertEquals(original.getGatewayPaymentId(), roundTripped.getGatewayPaymentId());
        assertEquals(original.getCheckoutId(), roundTripped.getCheckoutId());
        assertEquals(original.getIdempotencyKey(), roundTripped.getIdempotencyKey());
        assertEquals(original.getRefundAmount(), roundTripped.getRefundAmount());
        assertEquals(original.getRefundReason(), roundTripped.getRefundReason());
    }

    @Test
    void roundTrip_modelToEntityToModel_preservesCoreFields() {
        Payment original = buildFullModel();
        PaymentEntity entity = mapper.toEntity(original);
        Payment roundTripped = mapper.toModel(entity);

        assertEquals(original.getId(), roundTripped.getId());
        assertEquals(original.getOrderId(), roundTripped.getOrderId());
        assertEquals(original.getCustomerId(), roundTripped.getCustomerId());
        assertEquals(original.getAmount(), roundTripped.getAmount());
        assertEquals(original.getCurrency(), roundTripped.getCurrency());
        assertEquals(original.getPaymentMethod(), roundTripped.getPaymentMethod());
        assertEquals(original.getGatewayTransactionId(), roundTripped.getGatewayTransactionId());
        assertEquals(original.getRetryCount(), roundTripped.getRetryCount());
        assertEquals(original.getFailureReason(), roundTripped.getFailureReason());
        // New fields
        assertEquals(original.getPaymentMethodType(), roundTripped.getPaymentMethodType());
        assertEquals(original.getGatewayName(), roundTripped.getGatewayName());
        assertEquals(original.getCheckoutId(), roundTripped.getCheckoutId());
        assertEquals(original.getIdempotencyKey(), roundTripped.getIdempotencyKey());
    }

    @Test
    void roundTrip_activities_preserved() {
        PaymentEntity original = new PaymentEntity();
        original.setId("pay-1");
        original.setAmount(BigDecimal.TEN);

        PaymentActivityLogEntity log = new PaymentActivityLogEntity();
        log.setEventId("succeed");
        log.setComment("Payment confirmed");
        log.setSuccess(true);
        original.setActivities(List.of(log));

        Payment model = mapper.toModel(original);
        PaymentEntity roundTripped = mapper.toEntity(model);

        assertEquals(1, roundTripped.getActivities().size());
        assertEquals("succeed", roundTripped.getActivities().get(0).getName());
    }

    @Test
    void toModel_emptyCollections_mappedAsEmpty() {
        PaymentEntity entity = new PaymentEntity();
        entity.setId("pay-1");
        entity.setAmount(BigDecimal.ZERO);
        entity.setActivities(new ArrayList<>());

        Payment model = mapper.toModel(entity);

        assertTrue(model.getActivities().isEmpty());
    }

    @Test
    void toEntity_bigDecimalAmount_preserved() {
        Payment model = new Payment();
        model.setId("pay-1");
        model.setAmount(new BigDecimal("999999.99"));

        PaymentEntity entity = mapper.toEntity(model);

        assertEquals(new BigDecimal("999999.99"), entity.getAmount());
    }

    @Test
    void toModel_versionMapped() {
        PaymentEntity entity = new PaymentEntity();
        entity.setId("pay-1");
        entity.setAmount(BigDecimal.ZERO);
        entity.setVersion(5L);

        Payment model = mapper.toModel(entity);
        assertEquals(5L, model.getVersion());
    }

    // ── Helpers ────────────────────────────────────────────────────────────

    private PaymentEntity buildFullEntity() {
        PaymentEntity entity = new PaymentEntity();
        entity.setId("pay-1");
        entity.setOrderId("ORD-001");
        entity.setCustomerId("CUST-001");
        entity.setAmount(new BigDecimal("2500.00"));
        entity.setCurrency("INR");
        entity.setPaymentMethod("UPI");
        entity.setGatewayTransactionId("gw-txn-001");
        entity.setGatewayResponse("{\"status\":\"captured\"}");
        entity.setRetryCount(2);
        entity.setFailureReason("Insufficient funds");
        entity.setCurrentState(new State("SUCCEEDED", "payment-flow"));
        entity.setCreatedTime(new Date());
        entity.setLastModifiedTime(new Date());
        // New fields
        entity.setPaymentMethodType("CREDIT_CARD");
        entity.setCardLastFour("4242");
        entity.setCardBrand("VISA");
        entity.setUpiId("priya@okicici");
        entity.setGatewayName("RAZORPAY");
        entity.setGatewayOrderId("order_RPay_001");
        entity.setGatewayPaymentId("pay_RPay_001");
        entity.setCheckoutId("chk-001");
        entity.setIdempotencyKey("idem-001");
        entity.setRefundAmount(new BigDecimal("500.00"));
        entity.setRefundReason("Product defective");
        return entity;
    }

    private Payment buildFullModel() {
        Payment model = new Payment();
        model.setId("pay-1");
        model.setOrderId("ORD-001");
        model.setCustomerId("CUST-001");
        model.setAmount(new BigDecimal("2500.00"));
        model.setCurrency("INR");
        model.setPaymentMethod("UPI");
        model.setGatewayTransactionId("gw-txn-001");
        model.setGatewayResponse("{\"status\":\"captured\"}");
        model.setRetryCount(2);
        model.setFailureReason("Insufficient funds");
        model.setCurrentState(new State("SUCCEEDED", "payment-flow"));
        // New fields
        model.setPaymentMethodType("CREDIT_CARD");
        model.setCardLastFour("4242");
        model.setCardBrand("VISA");
        model.setUpiId("priya@okicici");
        model.setGatewayName("RAZORPAY");
        model.setGatewayOrderId("order_RPay_001");
        model.setGatewayPaymentId("pay_RPay_001");
        model.setCheckoutId("chk-001");
        model.setIdempotencyKey("idem-001");
        model.setRefundAmount(new BigDecimal("500.00"));
        model.setRefundReason("Product defective");
        return model;
    }
}
