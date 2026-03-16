package com.homebase.ecom.inventory.infrastructure.persistence.mapper;

import com.homebase.ecom.inventory.domain.model.DamageRecord;
import com.homebase.ecom.inventory.domain.model.DamageStatus;
import com.homebase.ecom.inventory.domain.model.InventoryActivityLog;
import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.domain.model.InventoryStatus;
import com.homebase.ecom.inventory.domain.model.MovementType;
import com.homebase.ecom.inventory.domain.model.Reservation;
import com.homebase.ecom.inventory.domain.model.ReservationStatus;
import com.homebase.ecom.inventory.domain.model.StockMovement;
import com.homebase.ecom.inventory.infrastructure.persistence.entity.DamageRecordEntity;
import com.homebase.ecom.inventory.infrastructure.persistence.entity.InventoryItemActivityLogEntity;
import com.homebase.ecom.inventory.infrastructure.persistence.entity.InventoryItemEntity;
import com.homebase.ecom.inventory.infrastructure.persistence.entity.InventoryReservationEntity;
import com.homebase.ecom.inventory.infrastructure.persistence.entity.StockMovementEntity;
import org.chenile.stm.State;
import org.chenile.workflow.activities.model.ActivityLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryItemMapperTest {

    private InventoryItemMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new InventoryItemMapper();
    }

    // ---- Entity -> Model tests ----

    @Test
    void toModel_nullEntity_returnsNull() {
        assertNull(mapper.toModel(null));
    }

    @Test
    void toModel_fullEntity_mapsAllCoreFields() {
        InventoryItemEntity entity = buildFullEntity();
        InventoryItem model = mapper.toModel(entity);

        assertEquals("inv-1", model.getId());
        assertEquals("Test item", model.getDescription());
        assertEquals("SKU-100", model.getSku());
        assertEquals("ASIN-100", model.getAsin());
        assertEquals("prod-1", model.getProductId());
        assertEquals("var-1", model.getVariantId());
        assertEquals(Integer.valueOf(100), model.getQuantity());
        assertEquals(Integer.valueOf(80), model.getAvailableQuantity());
        assertEquals(Integer.valueOf(10), model.getReservedQuantity());
        assertEquals(Integer.valueOf(5), model.getInboundQuantity());
        assertEquals(Integer.valueOf(5), model.getDamagedQuantity());
        assertEquals("FC-WEST", model.getPrimaryFulfillmentCenter());
        assertTrue(model.getIsFbaEnabled());
        assertFalse(model.getIsMerchantFulfilled());
        assertEquals(Integer.valueOf(15), model.getLowStockThreshold());
        assertEquals(Integer.valueOf(2), model.getOutOfStockThreshold());
        assertEquals(InventoryStatus.AVAILABLE, model.getStatus());
        assertNotNull(model.getLastSaleAt());
        assertNotNull(model.getLastRestockAt());
        assertEquals(new State("IN_STOCK", "default"), model.getCurrentState());
    }

    @Test
    void toModel_mapsReservations() {
        InventoryItemEntity entity = new InventoryItemEntity();
        entity.setId("inv-1");

        InventoryReservationEntity re = new InventoryReservationEntity();
        re.setOrderId("ord-1");
        re.setSessionId("sess-1");
        re.setQuantity(3);
        Instant now = Instant.now();
        re.setReservedAt(now);
        re.setExpiresAt(now.plusSeconds(3600));
        re.setStatus(ReservationStatus.RESERVED);
        entity.setActiveReservations(List.of(re));

        InventoryItem model = mapper.toModel(entity);

        assertEquals(1, model.getActiveReservations().size());
        Reservation r = model.getActiveReservations().get(0);
        assertEquals("ord-1", r.orderId());
        assertEquals("sess-1", r.sessionId());
        assertEquals(Integer.valueOf(3), r.quantity());
        assertEquals(now, r.reservedAt());
        assertEquals(now.plusSeconds(3600), r.expiresAt());
        assertEquals(ReservationStatus.RESERVED, r.status());
    }

    @Test
    void toModel_mapsMovementHistory() {
        InventoryItemEntity entity = new InventoryItemEntity();
        entity.setId("inv-1");

        StockMovementEntity sme = new StockMovementEntity();
        sme.setType(MovementType.RECEIVE);
        sme.setQuantity(50);
        sme.setReferenceId("po-1");
        sme.setFulfillmentCenterId("FC-EAST");
        Instant movTime = Instant.now();
        sme.setMovementTime(movTime);
        sme.setReason("Initial stock");
        entity.setMovementHistory(List.of(sme));

        InventoryItem model = mapper.toModel(entity);

        assertEquals(1, model.getMovementHistory().size());
        StockMovement sm = model.getMovementHistory().get(0);
        assertEquals(MovementType.RECEIVE, sm.type());
        assertEquals(Integer.valueOf(50), sm.quantity());
        assertEquals("po-1", sm.referenceId());
        assertEquals("FC-EAST", sm.fulfillmentCenterId());
        assertEquals(movTime, sm.movementTime());
        assertEquals("Initial stock", sm.reason());
    }

    @Test
    void toModel_mapsDamageRecords() {
        InventoryItemEntity entity = new InventoryItemEntity();
        entity.setId("inv-1");

        DamageRecordEntity dre = new DamageRecordEntity();
        dre.setUnitIdentifier("UNIT-001");
        dre.setLocation("Shelf-A3");
        dre.setDamageType("SCRATCH");
        dre.setDescription("Surface scratch on panel");
        Instant discovered = Instant.now();
        dre.setDiscoveredAt(discovered);
        dre.setStatus(DamageStatus.REPORTED);
        entity.setDamageRecords(List.of(dre));

        InventoryItem model = mapper.toModel(entity);

        assertEquals(1, model.getDamageRecords().size());
        DamageRecord dr = model.getDamageRecords().get(0);
        assertEquals("UNIT-001", dr.unitIdentifier());
        assertEquals("Shelf-A3", dr.location());
        assertEquals("SCRATCH", dr.damageType());
        assertEquals("Surface scratch on panel", dr.description());
        assertEquals(discovered, dr.discoveredAt());
        assertEquals(DamageStatus.REPORTED, dr.status());
    }

    @Test
    void toModel_mapsActivities() {
        InventoryItemEntity entity = new InventoryItemEntity();
        entity.setId("inv-1");

        InventoryItemActivityLogEntity log = new InventoryItemActivityLogEntity();
        log.setEventId("RECEIVE_STOCK");
        log.setComment("50 units received");
        log.setSuccess(true);
        entity.setActivities(List.of(log));

        InventoryItem model = mapper.toModel(entity);

        assertEquals(1, model.getActivities().size());
        ActivityLog al = model.getActivities().get(0);
        assertEquals("RECEIVE_STOCK", al.getName());
        assertEquals("50 units received", al.getComment());
        assertTrue(al.getSuccess());
    }

    @Test
    void toModel_nullCollections_noExceptions() {
        InventoryItemEntity entity = new InventoryItemEntity();
        entity.setId("inv-1");
        entity.setActiveReservations(null);
        entity.setMovementHistory(null);
        entity.setDamageRecords(null);
        entity.setActivities(null);

        InventoryItem model = mapper.toModel(entity);

        assertNotNull(model);
        assertEquals("inv-1", model.getId());
    }

    @Test
    void toModel_emptyStringFields_preservedAsEmpty() {
        InventoryItemEntity entity = new InventoryItemEntity();
        entity.setId("inv-1");
        entity.setDescription("");
        entity.setSku("");
        entity.setAsin("");

        InventoryItem model = mapper.toModel(entity);

        assertEquals("", model.getDescription());
        assertEquals("", model.getSku());
        assertEquals("", model.getAsin());
    }

    @Test
    void toModel_zeroQuantities_preserved() {
        InventoryItemEntity entity = new InventoryItemEntity();
        entity.setId("inv-1");
        entity.setQuantity(0);
        entity.setAvailableQuantity(0);
        entity.setReservedQuantity(0);
        entity.setInboundQuantity(0);
        entity.setDamagedQuantity(0);

        InventoryItem model = mapper.toModel(entity);

        assertEquals(Integer.valueOf(0), model.getQuantity());
        assertEquals(Integer.valueOf(0), model.getAvailableQuantity());
        assertEquals(Integer.valueOf(0), model.getReservedQuantity());
        assertEquals(Integer.valueOf(0), model.getInboundQuantity());
        assertEquals(Integer.valueOf(0), model.getDamagedQuantity());
    }

    @Test
    void toModel_stateFlowFieldsMapped() {
        InventoryItemEntity entity = new InventoryItemEntity();
        entity.setId("inv-1");
        State state = new State("RECEIVING", "inbound-flow");
        entity.setCurrentState(state);

        InventoryItem model = mapper.toModel(entity);

        assertNotNull(model.getCurrentState());
        assertEquals("RECEIVING", model.getCurrentState().getStateId());
        assertEquals("inbound-flow", model.getCurrentState().getFlowId());
    }

    @Test
    void toModel_nullCurrentState_preservedAsNull() {
        InventoryItemEntity entity = new InventoryItemEntity();
        entity.setId("inv-1");
        entity.setCurrentState(null);

        InventoryItem model = mapper.toModel(entity);

        assertNull(model.getCurrentState());
    }

    @Test
    void toModel_createdAndModifiedTimes_mapped() {
        InventoryItemEntity entity = new InventoryItemEntity();
        entity.setId("inv-1");
        Date created = new Date(1000000L);
        Date modified = new Date(2000000L);
        entity.setCreatedTime(created);
        entity.setLastModifiedTime(modified);

        InventoryItem model = mapper.toModel(entity);

        assertEquals(created, model.getCreatedTime());
        assertEquals(modified, model.getLastModifiedTime());
    }

    @Test
    void toModel_nullTimestamps_noException() {
        InventoryItemEntity entity = new InventoryItemEntity();
        entity.setId("inv-1");
        entity.setCreatedTime(null);
        entity.setLastModifiedTime(null);
        entity.setLastSaleAt(null);
        entity.setLastRestockAt(null);

        InventoryItem model = mapper.toModel(entity);

        assertNotNull(model);
        // createdTime/lastModifiedTime not set when source is null (conditional in mapper)
    }

    // ---- Model -> Entity tests ----

    @Test
    void toEntity_nullModel_returnsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toEntity_fullModel_mapsAllCoreFields() {
        InventoryItem model = buildFullModel();
        InventoryItemEntity entity = mapper.toEntity(model);

        assertEquals("inv-1", entity.getId());
        assertEquals("Test item", entity.getDescription());
        assertEquals("SKU-100", entity.getSku());
        assertEquals("ASIN-100", entity.getAsin());
        assertEquals("prod-1", entity.getProductId());
        assertEquals("var-1", entity.getVariantId());
        assertEquals(Integer.valueOf(100), entity.getQuantity());
        assertEquals(Integer.valueOf(80), entity.getAvailableQuantity());
        assertEquals(Integer.valueOf(10), entity.getReservedQuantity());
        assertEquals(Integer.valueOf(5), entity.getInboundQuantity());
        assertEquals(Integer.valueOf(5), entity.getDamagedQuantity());
        assertEquals("FC-WEST", entity.getPrimaryFulfillmentCenter());
        assertTrue(entity.getIsFbaEnabled());
        assertFalse(entity.getIsMerchantFulfilled());
        assertEquals(Integer.valueOf(15), entity.getLowStockThreshold());
        assertEquals(Integer.valueOf(2), entity.getOutOfStockThreshold());
        assertEquals(InventoryStatus.AVAILABLE, entity.getStatus());
        assertNotNull(entity.getLastSaleAt());
        assertNotNull(entity.getLastRestockAt());
        assertEquals(new State("IN_STOCK", "default"), entity.getCurrentState());
    }

    @Test
    void toEntity_mapsReservations() {
        InventoryItem model = new InventoryItem();
        model.setId("inv-1");

        Instant now = Instant.now();
        Reservation r = new Reservation("ord-1", "sess-1", 5, now, now.plusSeconds(1800), ReservationStatus.RESERVED);
        model.setActiveReservations(List.of(r));

        InventoryItemEntity entity = mapper.toEntity(model);

        assertEquals(1, entity.getActiveReservations().size());
        InventoryReservationEntity re = entity.getActiveReservations().get(0);
        assertEquals("ord-1", re.getOrderId());
        assertEquals("sess-1", re.getSessionId());
        assertEquals(Integer.valueOf(5), re.getQuantity());
        assertEquals(now, re.getReservedAt());
        assertEquals(now.plusSeconds(1800), re.getExpiresAt());
        assertEquals(ReservationStatus.RESERVED, re.getStatus());
    }

    @Test
    void toEntity_mapsMovementHistory() {
        InventoryItem model = new InventoryItem();
        model.setId("inv-1");

        Instant movTime = Instant.now();
        StockMovement sm = new StockMovement(MovementType.PICK, 10, "ord-1", "FC-EAST", movTime, "Order fulfilled");
        model.setMovementHistory(List.of(sm));

        InventoryItemEntity entity = mapper.toEntity(model);

        assertEquals(1, entity.getMovementHistory().size());
        StockMovementEntity sme = entity.getMovementHistory().get(0);
        assertEquals(MovementType.PICK, sme.getType());
        assertEquals(Integer.valueOf(10), sme.getQuantity());
        assertEquals("ord-1", sme.getReferenceId());
        assertEquals("FC-EAST", sme.getFulfillmentCenterId());
        assertEquals(movTime, sme.getMovementTime());
        assertEquals("Order fulfilled", sme.getReason());
    }

    @Test
    void toEntity_mapsDamageRecords() {
        InventoryItem model = new InventoryItem();
        model.setId("inv-1");

        Instant discovered = Instant.now();
        DamageRecord dr = new DamageRecord("UNIT-002", "Shelf-B1", "DENT", "Dent on corner", discovered, DamageStatus.REPORTED);
        model.setDamageRecords(List.of(dr));

        InventoryItemEntity entity = mapper.toEntity(model);

        assertEquals(1, entity.getDamageRecords().size());
        DamageRecordEntity dre = entity.getDamageRecords().get(0);
        assertEquals("UNIT-002", dre.getUnitIdentifier());
        assertEquals("Shelf-B1", dre.getLocation());
        assertEquals("DENT", dre.getDamageType());
        assertEquals("Dent on corner", dre.getDescription());
        assertEquals(discovered, dre.getDiscoveredAt());
        assertEquals(DamageStatus.REPORTED, dre.getStatus());
    }

    @Test
    void toEntity_mapsActivities() {
        InventoryItem model = new InventoryItem();
        model.setId("inv-1");

        InventoryActivityLog log = new InventoryActivityLog();
        log.activityName = "RESTOCK";
        log.activityComment = "Restocked 25 units";
        log.activitySuccess = true;
        model.setActivities(List.of(log));

        InventoryItemEntity entity = mapper.toEntity(model);

        assertEquals(1, entity.getActivities().size());
        InventoryItemActivityLogEntity ale = entity.getActivities().get(0);
        assertEquals("RESTOCK", ale.getName());
        assertEquals("Restocked 25 units", ale.getComment());
        assertTrue(ale.getSuccess());
    }

    @Test
    void toEntity_nullCollections_noExceptions() {
        InventoryItem model = new InventoryItem();
        model.setId("inv-1");
        model.setActiveReservations(null);
        model.setMovementHistory(null);
        model.setDamageRecords(null);
        model.setActivities(null);

        InventoryItemEntity entity = mapper.toEntity(model);

        assertNotNull(entity);
        assertEquals("inv-1", entity.getId());
    }

    @Test
    void toEntity_stateFlowFieldsMapped() {
        InventoryItem model = new InventoryItem();
        model.setId("inv-1");
        State state = new State("LOW_STOCK", "alert-flow");
        model.setCurrentState(state);

        InventoryItemEntity entity = mapper.toEntity(model);

        assertNotNull(entity.getCurrentState());
        assertEquals("LOW_STOCK", entity.getCurrentState().getStateId());
        assertEquals("alert-flow", entity.getCurrentState().getFlowId());
    }

    @Test
    void toEntity_zeroQuantities_preserved() {
        InventoryItem model = new InventoryItem();
        model.setId("inv-1");
        model.setQuantity(0);
        model.setAvailableQuantity(0);
        model.setReservedQuantity(0);

        InventoryItemEntity entity = mapper.toEntity(model);

        assertEquals(Integer.valueOf(0), entity.getQuantity());
        assertEquals(Integer.valueOf(0), entity.getAvailableQuantity());
        assertEquals(Integer.valueOf(0), entity.getReservedQuantity());
    }

    @Test
    void toEntity_booleanFlags_preserved() {
        InventoryItem model = new InventoryItem();
        model.setId("inv-1");
        model.setIsFbaEnabled(false);
        model.setIsMerchantFulfilled(true);

        InventoryItemEntity entity = mapper.toEntity(model);

        assertFalse(entity.getIsFbaEnabled());
        assertTrue(entity.getIsMerchantFulfilled());
    }

    // ---- Round-trip tests ----

    @Test
    void roundTrip_entityToModelToEntity_preservesCoreFields() {
        InventoryItemEntity original = buildFullEntity();
        InventoryItem model = mapper.toModel(original);
        InventoryItemEntity roundTripped = mapper.toEntity(model);

        assertEquals(original.getId(), roundTripped.getId());
        assertEquals(original.getDescription(), roundTripped.getDescription());
        assertEquals(original.getSku(), roundTripped.getSku());
        assertEquals(original.getAsin(), roundTripped.getAsin());
        assertEquals(original.getProductId(), roundTripped.getProductId());
        assertEquals(original.getVariantId(), roundTripped.getVariantId());
        assertEquals(original.getQuantity(), roundTripped.getQuantity());
        assertEquals(original.getAvailableQuantity(), roundTripped.getAvailableQuantity());
        assertEquals(original.getReservedQuantity(), roundTripped.getReservedQuantity());
        assertEquals(original.getInboundQuantity(), roundTripped.getInboundQuantity());
        assertEquals(original.getDamagedQuantity(), roundTripped.getDamagedQuantity());
        assertEquals(original.getPrimaryFulfillmentCenter(), roundTripped.getPrimaryFulfillmentCenter());
        assertEquals(original.getIsFbaEnabled(), roundTripped.getIsFbaEnabled());
        assertEquals(original.getIsMerchantFulfilled(), roundTripped.getIsMerchantFulfilled());
        assertEquals(original.getLowStockThreshold(), roundTripped.getLowStockThreshold());
        assertEquals(original.getOutOfStockThreshold(), roundTripped.getOutOfStockThreshold());
        assertEquals(original.getCurrentState(), roundTripped.getCurrentState());
        assertEquals(original.getLastSaleAt(), roundTripped.getLastSaleAt());
        assertEquals(original.getLastRestockAt(), roundTripped.getLastRestockAt());
    }

    @Test
    void roundTrip_reservations_preserved() {
        InventoryItemEntity original = new InventoryItemEntity();
        original.setId("inv-1");
        original.setQuantity(10);
        original.setAvailableQuantity(5);
        original.setReservedQuantity(5);
        Instant now = Instant.now();
        InventoryReservationEntity re = new InventoryReservationEntity();
        re.setOrderId("ord-1");
        re.setSessionId("sess-1");
        re.setQuantity(7);
        re.setReservedAt(now);
        re.setExpiresAt(now.plusSeconds(600));
        re.setStatus(ReservationStatus.CONFIRMED);
        original.setActiveReservations(List.of(re));

        InventoryItem model = mapper.toModel(original);
        InventoryItemEntity roundTripped = mapper.toEntity(model);

        assertEquals(1, roundTripped.getActiveReservations().size());
        InventoryReservationEntity rr = roundTripped.getActiveReservations().get(0);
        assertEquals("ord-1", rr.getOrderId());
        assertEquals("sess-1", rr.getSessionId());
        assertEquals(Integer.valueOf(7), rr.getQuantity());
        assertEquals(ReservationStatus.CONFIRMED, rr.getStatus());
    }

    @Test
    void toModel_emptyCollections_mappedAsEmpty() {
        InventoryItemEntity entity = new InventoryItemEntity();
        entity.setId("inv-1");
        entity.setActiveReservations(new ArrayList<>());
        entity.setMovementHistory(new ArrayList<>());
        entity.setDamageRecords(new ArrayList<>());
        entity.setActivities(new ArrayList<>());

        InventoryItem model = mapper.toModel(entity);

        assertTrue(model.getActiveReservations().isEmpty());
        assertTrue(model.getMovementHistory().isEmpty());
        assertTrue(model.getDamageRecords().isEmpty());
        assertTrue(model.getActivities().isEmpty());
    }

    @Test
    void toEntity_discontinuedStatus_preserved() {
        InventoryItem model = new InventoryItem();
        model.setId("inv-1");
        model.setStatus(InventoryStatus.DISCONTINUED);

        InventoryItemEntity entity = mapper.toEntity(model);

        assertEquals(InventoryStatus.DISCONTINUED, entity.getStatus());
    }

    @Test
    void toModel_multipleReservations_allMapped() {
        InventoryItemEntity entity = new InventoryItemEntity();
        entity.setId("inv-1");

        Instant now = Instant.now();
        InventoryReservationEntity re1 = new InventoryReservationEntity();
        re1.setOrderId("ord-1");
        re1.setSessionId("s1");
        re1.setQuantity(2);
        re1.setReservedAt(now);
        re1.setExpiresAt(now.plusSeconds(600));
        re1.setStatus(ReservationStatus.RESERVED);

        InventoryReservationEntity re2 = new InventoryReservationEntity();
        re2.setOrderId("ord-2");
        re2.setSessionId("s2");
        re2.setQuantity(3);
        re2.setReservedAt(now);
        re2.setExpiresAt(now.plusSeconds(600));
        re2.setStatus(ReservationStatus.FULFILLED);

        entity.setActiveReservations(List.of(re1, re2));

        InventoryItem model = mapper.toModel(entity);

        assertEquals(2, model.getActiveReservations().size());
        assertEquals("ord-1", model.getActiveReservations().get(0).orderId());
        assertEquals("ord-2", model.getActiveReservations().get(1).orderId());
    }

    // ---- Helpers ----

    private InventoryItemEntity buildFullEntity() {
        InventoryItemEntity entity = new InventoryItemEntity();
        entity.setId("inv-1");
        entity.setDescription("Test item");
        entity.setSku("SKU-100");
        entity.setAsin("ASIN-100");
        entity.setProductId("prod-1");
        entity.setVariantId("var-1");
        entity.setQuantity(100);
        entity.setAvailableQuantity(80);
        entity.setReservedQuantity(10);
        entity.setInboundQuantity(5);
        entity.setDamagedQuantity(5);
        entity.setPrimaryFulfillmentCenter("FC-WEST");
        entity.setIsFbaEnabled(true);
        entity.setIsMerchantFulfilled(false);
        entity.setLowStockThreshold(15);
        entity.setOutOfStockThreshold(2);
        entity.setStatus(InventoryStatus.AVAILABLE);
        entity.setLastSaleAt(Instant.now());
        entity.setLastRestockAt(Instant.now());
        entity.setCurrentState(new State("IN_STOCK", "default"));
        entity.setCreatedTime(new Date());
        entity.setLastModifiedTime(new Date());
        return entity;
    }

    private InventoryItem buildFullModel() {
        InventoryItem model = new InventoryItem();
        model.setId("inv-1");
        model.setDescription("Test item");
        model.setSku("SKU-100");
        model.setAsin("ASIN-100");
        model.setProductId("prod-1");
        model.setVariantId("var-1");
        model.setQuantity(100);
        model.setAvailableQuantity(80);
        model.setReservedQuantity(10);
        model.setInboundQuantity(5);
        model.setDamagedQuantity(5);
        model.setPrimaryFulfillmentCenter("FC-WEST");
        model.setIsFbaEnabled(true);
        model.setIsMerchantFulfilled(false);
        model.setLowStockThreshold(15);
        model.setOutOfStockThreshold(2);
        model.setStatus(InventoryStatus.AVAILABLE);
        model.setLastSaleAt(Instant.now());
        model.setLastRestockAt(Instant.now());
        model.setCurrentState(new State("IN_STOCK", "default"));
        return model;
    }
}
