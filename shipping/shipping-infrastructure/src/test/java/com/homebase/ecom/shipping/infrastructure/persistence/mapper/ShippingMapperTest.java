package com.homebase.ecom.shipping.infrastructure.persistence.mapper;

import com.homebase.ecom.shipping.model.Shipping;
import com.homebase.ecom.shipping.infrastructure.persistence.entity.ShippingEntity;
import com.homebase.ecom.shipping.infrastructure.persistence.entity.ShippingActivityLogEntity;
import org.chenile.stm.State;
import org.chenile.workflow.activities.model.ActivityLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShippingMapperTest {

    private ShippingMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ShippingMapper();
    }

    // ---- Entity -> Model tests ----

    @Test
    void toModel_nullEntity_returnsNull() {
        assertNull(mapper.toModel(null));
    }

    @Test
    void toModel_fullEntity_mapsAllCoreFields() {
        ShippingEntity entity = buildFullEntity();
        Shipping model = mapper.toModel(entity);

        assertEquals("ship-1", model.getId());
        assertEquals("ord-1", model.getOrderId());
        assertEquals("cust-1", model.getCustomerId());
        assertEquals("TRK-001", model.getTrackingNumber());
        assertEquals("DHL EXPRESS", model.getCarrier());
        assertEquals("EXPRESS", model.getShippingMethod());
        assertEquals("{\"street\":\"123 Warehouse\"}", model.getFromAddress());
        assertEquals("{\"street\":\"456 Customer\"}", model.getToAddress());
        assertEquals("2.5kg", model.getWeight());
        assertEquals("30x20x15", model.getDimensions());
        assertNotNull(model.getEstimatedDeliveryDate());
        assertNotNull(model.getActualDeliveryDate());
        assertEquals(1, model.getDeliveryAttempts());
        assertEquals("Leave at door", model.getDeliveryInstructions());
        assertEquals("Mumbai Hub", model.getCurrentLocation());
        assertEquals("Test shipment", model.description);
        assertEquals(new State("IN_TRANSIT", "shipping-flow"), model.getCurrentState());
    }

    @Test
    void toModel_mapsActivities() {
        ShippingEntity entity = new ShippingEntity();
        entity.setId("ship-1");
        entity.setCarrier("DHL");

        ShippingActivityLogEntity log = new ShippingActivityLogEntity();
        log.activityName = "createLabel";
        log.activityComment = "Label created";
        log.activitySuccess = true;
        entity.activities = List.of(log);

        Shipping model = mapper.toModel(entity);

        assertEquals(1, model.obtainActivities().size());
        ActivityLog al = model.obtainActivities().iterator().next();
        assertEquals("createLabel", al.getName());
        assertEquals("Label created", al.getComment());
        assertTrue(al.getSuccess());
    }

    @Test
    void toModel_nullActivities_noException() {
        ShippingEntity entity = new ShippingEntity();
        entity.setId("ship-1");
        entity.setCarrier("DHL");
        entity.activities = null;

        Shipping model = mapper.toModel(entity);

        assertNotNull(model);
        assertEquals("ship-1", model.getId());
    }

    @Test
    void toModel_emptyStringFields_preservedAsEmpty() {
        ShippingEntity entity = new ShippingEntity();
        entity.setId("ship-1");
        entity.setCarrier("");
        entity.setDescription("");

        Shipping model = mapper.toModel(entity);

        assertEquals("", model.getCarrier());
        assertEquals("", model.description);
    }

    @Test
    void toModel_zeroDeliveryAttempts_preserved() {
        ShippingEntity entity = new ShippingEntity();
        entity.setId("ship-1");
        entity.setCarrier("DHL");
        entity.setDeliveryAttempts(0);

        Shipping model = mapper.toModel(entity);

        assertEquals(0, model.getDeliveryAttempts());
    }

    @Test
    void toModel_stateFieldsMapped() {
        ShippingEntity entity = new ShippingEntity();
        entity.setId("ship-1");
        entity.setCarrier("DHL");
        State state = new State("DELIVERED", "shipping-flow");
        entity.setCurrentState(state);

        Shipping model = mapper.toModel(entity);

        assertNotNull(model.getCurrentState());
        assertEquals("DELIVERED", model.getCurrentState().getStateId());
        assertEquals("shipping-flow", model.getCurrentState().getFlowId());
    }

    @Test
    void toModel_nullCurrentState_preservedAsNull() {
        ShippingEntity entity = new ShippingEntity();
        entity.setId("ship-1");
        entity.setCarrier("DHL");
        entity.setCurrentState(null);

        Shipping model = mapper.toModel(entity);

        assertNull(model.getCurrentState());
    }

    // ---- Model -> Entity tests ----

    @Test
    void toEntity_nullModel_returnsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toEntity_fullModel_mapsAllCoreFields() {
        Shipping model = buildFullModel();
        ShippingEntity entity = mapper.toEntity(model);

        assertEquals("ship-1", entity.getId());
        assertEquals("ord-1", entity.getOrderId());
        assertEquals("cust-1", entity.getCustomerId());
        assertEquals("TRK-001", entity.getTrackingNumber());
        assertEquals("DHL EXPRESS", entity.getCarrier());
        assertEquals("EXPRESS", entity.getShippingMethod());
        assertEquals("{\"street\":\"123 Warehouse\"}", entity.getFromAddress());
        assertEquals("{\"street\":\"456 Customer\"}", entity.getToAddress());
        assertEquals("2.5kg", entity.getWeight());
        assertEquals("30x20x15", entity.getDimensions());
        assertNotNull(entity.getEstimatedDeliveryDate());
        assertNotNull(entity.getActualDeliveryDate());
        assertEquals(1, entity.getDeliveryAttempts());
        assertEquals("Leave at door", entity.getDeliveryInstructions());
        assertEquals("Mumbai Hub", entity.getCurrentLocation());
        assertEquals("Test shipment", entity.getDescription());
        assertEquals(new State("IN_TRANSIT", "shipping-flow"), entity.getCurrentState());
    }

    @Test
    void toEntity_mapsActivities() {
        Shipping model = new Shipping();
        model.setId("ship-1");
        model.setCarrier("DHL");
        model.addActivity("pickUp", "Picked up from warehouse");

        ShippingEntity entity = mapper.toEntity(model);

        assertEquals(1, entity.activities.size());
        ShippingActivityLogEntity ale = entity.activities.get(0);
        assertEquals("pickUp", ale.activityName);
        assertEquals("Picked up from warehouse", ale.activityComment);
        assertTrue(ale.activitySuccess);
    }

    @Test
    void toEntity_nullActivities_noException() {
        Shipping model = new Shipping();
        model.setId("ship-1");
        model.setCarrier("DHL");
        // obtainActivities() returns empty list by default

        ShippingEntity entity = mapper.toEntity(model);

        assertNotNull(entity);
        assertTrue(entity.activities.isEmpty());
    }

    @Test
    void toEntity_stateFieldsMapped() {
        Shipping model = new Shipping();
        model.setId("ship-1");
        model.setCarrier("DHL");
        State state = new State("PENDING", "shipping-flow");
        model.setCurrentState(state);

        ShippingEntity entity = mapper.toEntity(model);

        assertNotNull(entity.getCurrentState());
        assertEquals("PENDING", entity.getCurrentState().getStateId());
    }

    @Test
    void toEntity_deliveryAttempts_preserved() {
        Shipping model = new Shipping();
        model.setId("ship-1");
        model.setCarrier("DHL");
        model.setDeliveryAttempts(3);

        ShippingEntity entity = mapper.toEntity(model);

        assertEquals(3, entity.getDeliveryAttempts());
    }

    // ---- Round-trip tests ----

    @Test
    void roundTrip_entityToModelToEntity_preservesCoreFields() {
        ShippingEntity original = buildFullEntity();
        Shipping model = mapper.toModel(original);
        ShippingEntity roundTripped = mapper.toEntity(model);

        assertEquals(original.getId(), roundTripped.getId());
        assertEquals(original.getOrderId(), roundTripped.getOrderId());
        assertEquals(original.getCustomerId(), roundTripped.getCustomerId());
        assertEquals(original.getTrackingNumber(), roundTripped.getTrackingNumber());
        assertEquals(original.getCarrier(), roundTripped.getCarrier());
        assertEquals(original.getShippingMethod(), roundTripped.getShippingMethod());
        assertEquals(original.getFromAddress(), roundTripped.getFromAddress());
        assertEquals(original.getToAddress(), roundTripped.getToAddress());
        assertEquals(original.getWeight(), roundTripped.getWeight());
        assertEquals(original.getDimensions(), roundTripped.getDimensions());
        assertEquals(original.getDeliveryAttempts(), roundTripped.getDeliveryAttempts());
        assertEquals(original.getDeliveryInstructions(), roundTripped.getDeliveryInstructions());
        assertEquals(original.getCurrentLocation(), roundTripped.getCurrentLocation());
        assertEquals(original.getDescription(), roundTripped.getDescription());
        assertEquals(original.getCurrentState(), roundTripped.getCurrentState());
    }

    @Test
    void roundTrip_activities_preserved() {
        ShippingEntity original = new ShippingEntity();
        original.setId("ship-1");
        original.setCarrier("DHL");
        ShippingActivityLogEntity log = new ShippingActivityLogEntity();
        log.activityName = "deliver";
        log.activityComment = "Delivered successfully";
        log.activitySuccess = true;
        original.activities = List.of(log);

        Shipping model = mapper.toModel(original);
        ShippingEntity roundTripped = mapper.toEntity(model);

        assertEquals(1, roundTripped.activities.size());
        assertEquals("deliver", roundTripped.activities.get(0).activityName);
    }

    @Test
    void toModel_multipleActivities_allMapped() {
        ShippingEntity entity = new ShippingEntity();
        entity.setId("ship-1");
        entity.setCarrier("DHL");

        ShippingActivityLogEntity log1 = new ShippingActivityLogEntity();
        log1.activityName = "createLabel";
        log1.activityComment = "Label created";
        log1.activitySuccess = true;

        ShippingActivityLogEntity log2 = new ShippingActivityLogEntity();
        log2.activityName = "pickUp";
        log2.activityComment = "Picked up";
        log2.activitySuccess = true;

        entity.activities = List.of(log1, log2);

        Shipping model = mapper.toModel(entity);

        assertEquals(2, model.obtainActivities().size());
    }

    @Test
    void toModel_emptyActivities_mappedAsEmpty() {
        ShippingEntity entity = new ShippingEntity();
        entity.setId("ship-1");
        entity.setCarrier("DHL");
        entity.activities = new ArrayList<>();

        Shipping model = mapper.toModel(entity);

        assertTrue(model.obtainActivities().isEmpty());
    }

    // ---- Helpers ----

    private ShippingEntity buildFullEntity() {
        ShippingEntity entity = new ShippingEntity();
        entity.setId("ship-1");
        entity.setOrderId("ord-1");
        entity.setCustomerId("cust-1");
        entity.setTrackingNumber("TRK-001");
        entity.setCarrier("DHL EXPRESS");
        entity.setShippingMethod("EXPRESS");
        entity.setFromAddress("{\"street\":\"123 Warehouse\"}");
        entity.setToAddress("{\"street\":\"456 Customer\"}");
        entity.setWeight("2.5kg");
        entity.setDimensions("30x20x15");
        entity.setEstimatedDeliveryDate(new Date());
        entity.setActualDeliveryDate(new Date());
        entity.setDeliveryAttempts(1);
        entity.setDeliveryInstructions("Leave at door");
        entity.setCurrentLocation("Mumbai Hub");
        entity.setDescription("Test shipment");
        entity.setCurrentState(new State("IN_TRANSIT", "shipping-flow"));
        return entity;
    }

    private Shipping buildFullModel() {
        Shipping model = new Shipping();
        model.setId("ship-1");
        model.setOrderId("ord-1");
        model.setCustomerId("cust-1");
        model.setTrackingNumber("TRK-001");
        model.setCarrier("DHL EXPRESS");
        model.setShippingMethod("EXPRESS");
        model.setFromAddress("{\"street\":\"123 Warehouse\"}");
        model.setToAddress("{\"street\":\"456 Customer\"}");
        model.setWeight("2.5kg");
        model.setDimensions("30x20x15");
        model.setEstimatedDeliveryDate(new Date());
        model.setActualDeliveryDate(new Date());
        model.setDeliveryAttempts(1);
        model.setDeliveryInstructions("Leave at door");
        model.setCurrentLocation("Mumbai Hub");
        model.description = "Test shipment";
        model.setCurrentState(new State("IN_TRANSIT", "shipping-flow"));
        return model;
    }
}
