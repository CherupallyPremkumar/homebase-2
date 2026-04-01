package com.homebase.ecom.notification.mapper;

import com.homebase.ecom.notification.domain.model.Notification;
import com.homebase.ecom.notification.infrastructure.persistence.entity.NotificationEntity;
import com.homebase.ecom.notification.infrastructure.persistence.mapper.NotificationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for NotificationMapper bidirectional mapping.
 */
class NotificationMapperTest {

    private NotificationMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new NotificationMapper();
    }

    @Test
    void toModel_nullEntity_returnsNull() {
        assertNull(mapper.toModel(null));
    }

    @Test
    void toEntity_nullModel_returnsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toModel_mapsAllFields() {
        NotificationEntity entity = createTestEntity();

        Notification model = mapper.toModel(entity);

        assertNotNull(model);
        assertEquals("notif-001", model.getId());
        assertEquals("cust-001", model.getCustomerId());
        assertEquals("EMAIL", model.getChannel());
        assertEquals("ORDER_CONFIRMATION", model.getTemplateId());
        assertEquals("Order Confirmed", model.getSubject());
        assertEquals("Your order has been confirmed.", model.getBody());
        assertEquals("customer@example.com", model.getRecipientAddress());
        assertEquals("order-123", model.getMetadata().get("orderId"));
        assertNotNull(model.getSentAt());
        assertNotNull(model.getDeliveredAt());
        assertEquals("Connection timeout", model.getFailureReason());
        assertEquals(2, model.getRetryCount());
    }

    @Test
    void toEntity_mapsAllFields() {
        Notification model = createTestModel();

        NotificationEntity entity = mapper.toEntity(model);

        assertNotNull(entity);
        assertEquals("notif-002", entity.getId());
        assertEquals("cust-002", entity.getCustomerId());
        assertEquals("SMS", entity.getChannel());
        assertEquals("PAYMENT_FAILED", entity.getTemplateId());
        assertEquals("Payment Failed", entity.getSubject());
        assertEquals("Your payment failed.", entity.getBody());
        assertEquals("+1234567890", entity.getRecipientAddress());
        assertEquals("order-456", entity.getMetadata().get("orderId"));
        assertNotNull(entity.getSentAt());
        assertNull(entity.getDeliveredAt());
        assertEquals("Gateway error", entity.getFailureReason());
        assertEquals(1, entity.getRetryCount());
    }

    @Test
    void roundTrip_model_entity_model() {
        Notification original = createTestModel();

        NotificationEntity entity = mapper.toEntity(original);
        Notification roundTripped = mapper.toModel(entity);

        assertEquals(original.getId(), roundTripped.getId());
        assertEquals(original.getCustomerId(), roundTripped.getCustomerId());
        assertEquals(original.getChannel(), roundTripped.getChannel());
        assertEquals(original.getTemplateId(), roundTripped.getTemplateId());
        assertEquals(original.getSubject(), roundTripped.getSubject());
        assertEquals(original.getBody(), roundTripped.getBody());
        assertEquals(original.getRecipientAddress(), roundTripped.getRecipientAddress());
        assertEquals(original.getFailureReason(), roundTripped.getFailureReason());
        assertEquals(original.getRetryCount(), roundTripped.getRetryCount());
    }

    @Test
    void roundTrip_entity_model_entity() {
        NotificationEntity original = createTestEntity();

        Notification model = mapper.toModel(original);
        NotificationEntity roundTripped = mapper.toEntity(model);

        assertEquals(original.getId(), roundTripped.getId());
        assertEquals(original.getCustomerId(), roundTripped.getCustomerId());
        assertEquals(original.getChannel(), roundTripped.getChannel());
        assertEquals(original.getTemplateId(), roundTripped.getTemplateId());
        assertEquals(original.getSubject(), roundTripped.getSubject());
        assertEquals(original.getBody(), roundTripped.getBody());
        assertEquals(original.getRecipientAddress(), roundTripped.getRecipientAddress());
        assertEquals(original.getFailureReason(), roundTripped.getFailureReason());
        assertEquals(original.getRetryCount(), roundTripped.getRetryCount());
    }

    @Test
    void toModel_emptyMetadata_returnsEmptyMap() {
        NotificationEntity entity = new NotificationEntity();
        entity.setId("notif-empty");
        entity.setMetadata(null);

        Notification model = mapper.toModel(entity);
        assertNotNull(model.getMetadata());
        assertTrue(model.getMetadata().isEmpty());
    }

    @Test
    void toEntity_emptyMetadata_returnsEmptyMap() {
        Notification model = new Notification();
        model.setId("notif-empty");
        model.setMetadata(null);

        NotificationEntity entity = mapper.toEntity(model);
        assertNotNull(entity.getMetadata());
        assertTrue(entity.getMetadata().isEmpty());
    }

    // ── Test Helpers ──────────────────────────────────────────────────────

    private NotificationEntity createTestEntity() {
        NotificationEntity entity = new NotificationEntity();
        entity.setId("notif-001");
        entity.setCustomerId("cust-001");
        entity.setChannel("EMAIL");
        entity.setTemplateId("ORDER_CONFIRMATION");
        entity.setSubject("Order Confirmed");
        entity.setBody("Your order has been confirmed.");
        entity.setRecipientAddress("customer@example.com");
        Map<String, String> metadata = new HashMap<>();
        metadata.put("orderId", "order-123");
        entity.setMetadata(metadata);
        entity.setSentAt(new Date());
        entity.setDeliveredAt(new Date());
        entity.setFailureReason("Connection timeout");
        entity.setRetryCount(2);
        return entity;
    }

    private Notification createTestModel() {
        Notification model = new Notification();
        model.setId("notif-002");
        model.setCustomerId("cust-002");
        model.setChannel("SMS");
        model.setTemplateId("PAYMENT_FAILED");
        model.setSubject("Payment Failed");
        model.setBody("Your payment failed.");
        model.setRecipientAddress("+1234567890");
        Map<String, String> metadata = new HashMap<>();
        metadata.put("orderId", "order-456");
        model.setMetadata(metadata);
        model.setSentAt(new Date());
        model.setDeliveredAt(null);
        model.setFailureReason("Gateway error");
        model.setRetryCount(1);
        return model;
    }
}
