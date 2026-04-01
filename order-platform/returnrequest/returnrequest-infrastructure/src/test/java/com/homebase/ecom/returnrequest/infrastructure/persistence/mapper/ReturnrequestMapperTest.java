package com.homebase.ecom.returnrequest.infrastructure.persistence.mapper;

import com.homebase.ecom.returnrequest.infrastructure.persistence.entity.ReturnrequestEntity;
import com.homebase.ecom.returnrequest.model.ReturnItem;
import com.homebase.ecom.returnrequest.model.Returnrequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ReturnrequestMapper bidirectional mapping.
 * Tests model->entity and entity->model conversions including
 * ReturnItem JSON serialization.
 */
class ReturnrequestMapperTest {

    private ReturnrequestMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ReturnrequestMapper();
    }

    @Test
    void testToEntity_nullModel_returnsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void testToModel_nullEntity_returnsNull() {
        assertNull(mapper.toModel(null));
    }

    @Test
    void testModelToEntityAndBack_allFields() {
        Returnrequest model = new Returnrequest();
        model.setId("RET-001");
        model.orderId = "ORD-001";
        model.customerId = "CUST-001";
        model.reason = "DAMAGED";
        model.returnType = "REFUND";
        model.totalRefundAmount = new BigDecimal("1500.00");
        model.restockingFee = BigDecimal.ZERO;
        model.description = "Test return";
        model.reviewerId = "SUPPORT-001";
        model.reviewNotes = "Reviewed and approved";
        model.rejectionReason = null;
        model.rejectionComment = null;
        model.warehouseId = "WH-001";
        model.conditionOnReceipt = "DAMAGED_AS_REPORTED";
        model.inspectorId = "INSP-001";
        model.inspectorNotes = "Damage confirmed";
        model.orderDeliveryDate = LocalDateTime.of(2026, 3, 1, 10, 0, 0);
        model.orderTotalValue = new BigDecimal("3000.00");

        // Add return items
        model.items = Arrays.asList(
            new ReturnItem("ITEM-001", "PROD-001", "VAR-001", 1, "DAMAGED", "DAMAGED"),
            new ReturnItem("ITEM-002", "PROD-002", null, 2, "DEFECTIVE", "USED")
        );

        // Add activity
        model.addActivity("reviewReturn", "Return reviewed");

        // Model -> Entity
        ReturnrequestEntity entity = mapper.toEntity(model);
        assertNotNull(entity);
        assertEquals("RET-001", entity.getId());
        assertEquals("ORD-001", entity.getOrderId());
        assertEquals("CUST-001", entity.getCustomerId());
        assertEquals("DAMAGED", entity.getReason());
        assertEquals("REFUND", entity.getReturnType());
        assertEquals(new BigDecimal("1500.00"), entity.getTotalRefundAmount());
        assertEquals(BigDecimal.ZERO, entity.getRestockingFee());
        assertEquals("Test return", entity.getDescription());
        assertEquals("SUPPORT-001", entity.getReviewerId());
        assertEquals("WH-001", entity.getWarehouseId());
        assertEquals("INSP-001", entity.getInspectorId());
        assertNotNull(entity.getItemsJson());
        assertTrue(entity.getItemsJson().contains("PROD-001"));
        assertTrue(entity.getItemsJson().contains("PROD-002"));
        assertEquals(1, entity.getActivities().size());

        // Entity -> Model (round-trip)
        Returnrequest roundTrip = mapper.toModel(entity);
        assertNotNull(roundTrip);
        assertEquals("RET-001", roundTrip.getId());
        assertEquals("ORD-001", roundTrip.orderId);
        assertEquals("CUST-001", roundTrip.customerId);
        assertEquals("DAMAGED", roundTrip.reason);
        assertEquals("REFUND", roundTrip.returnType);
        assertEquals(new BigDecimal("1500.00"), roundTrip.totalRefundAmount);
        assertEquals(BigDecimal.ZERO, roundTrip.restockingFee);
        assertEquals("WH-001", roundTrip.warehouseId);
        assertEquals("INSP-001", roundTrip.inspectorId);
        assertNotNull(roundTrip.items);
        assertEquals(2, roundTrip.items.size());
        assertEquals("ITEM-001", roundTrip.items.get(0).orderItemId);
        assertEquals("PROD-001", roundTrip.items.get(0).productId);
        assertEquals("VAR-001", roundTrip.items.get(0).variantId);
        assertEquals(1, roundTrip.items.get(0).quantity);
        assertEquals("ITEM-002", roundTrip.items.get(1).orderItemId);
        assertEquals(2, roundTrip.items.get(1).quantity);
    }

    @Test
    void testEmptyItems_serializesAndDeserializesCorrectly() {
        Returnrequest model = new Returnrequest();
        model.setId("RET-002");
        model.orderId = "ORD-002";
        model.customerId = "CUST-002";

        ReturnrequestEntity entity = mapper.toEntity(model);
        assertNull(entity.getItemsJson());

        Returnrequest roundTrip = mapper.toModel(entity);
        assertNotNull(roundTrip.items);
        assertTrue(roundTrip.items.isEmpty());
    }

    @Test
    void testActivities_roundTrip() {
        Returnrequest model = new Returnrequest();
        model.setId("RET-003");
        model.orderId = "ORD-003";
        model.addActivity("reviewReturn", "Review completed");
        model.addActivity("approveReturn", "Approved");

        ReturnrequestEntity entity = mapper.toEntity(model);
        assertEquals(2, entity.getActivities().size());
        assertEquals("reviewReturn", entity.getActivities().get(0).getName());
        assertEquals("approveReturn", entity.getActivities().get(1).getName());

        Returnrequest roundTrip = mapper.toModel(entity);
        assertEquals(2, roundTrip.obtainActivities().size());
    }
}
