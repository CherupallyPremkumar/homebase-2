package com.homebase.ecom.support.mapper;

import com.homebase.ecom.support.infrastructure.persistence.entity.SupportTicketEntity;
import com.homebase.ecom.support.infrastructure.persistence.entity.TicketMessageEntity;
import com.homebase.ecom.support.infrastructure.persistence.mapper.SupportTicketMapper;
import com.homebase.ecom.support.model.SupportTicket;
import com.homebase.ecom.support.model.TicketMessage;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Bidirectional mapper tests for SupportTicketMapper.
 * Ensures model <-> entity conversion preserves all fields.
 */
public class SupportTicketMapperTest {

    private final SupportTicketMapper mapper = new SupportTicketMapper();

    @Test
    public void testModelToEntity() {
        SupportTicket model = createSampleModel();

        SupportTicketEntity entity = mapper.toEntity(model);

        assertNotNull(entity);
        assertEquals("ticket-001", entity.getId());
        assertEquals("cust-001", entity.getCustomerId());
        assertEquals("order-123", entity.getOrderId());
        assertEquals("Test Subject", entity.getSubject());
        assertEquals("ORDER", entity.getCategory());
        assertEquals("HIGH", entity.getPriority());
        assertEquals("Test description", entity.getDescription());
        assertEquals("agent-001", entity.getAssignedAgentId());
        assertEquals(2, entity.getReopenCount());
        assertFalse(entity.isSlaBreached());
        assertFalse(entity.isAutoCloseReady());
        assertNotNull(entity.getResolvedAt());

        // Messages
        assertNotNull(entity.getMessages());
        assertEquals(1, entity.getMessages().size());
        assertEquals("msg-001", entity.getMessages().get(0).getId());
        assertEquals("Hello", entity.getMessages().get(0).getMessage());
        assertEquals("CUSTOMER", entity.getMessages().get(0).getSenderType());
    }

    @Test
    public void testEntityToModel() {
        SupportTicketEntity entity = createSampleEntity();

        SupportTicket model = mapper.toModel(entity);

        assertNotNull(model);
        assertEquals("ticket-002", model.getId());
        assertEquals("cust-002", model.getCustomerId());
        assertEquals("order-456", model.getOrderId());
        assertEquals("Entity Subject", model.getSubject());
        assertEquals("PAYMENT", model.getCategory());
        assertEquals("URGENT", model.getPriority());
        assertEquals("Entity description", model.getDescription());
        assertEquals("agent-002", model.getAssignedAgentId());
        assertEquals(1, model.getReopenCount());
        assertTrue(model.isSlaBreached());
        assertFalse(model.isAutoCloseReady());

        // Messages
        assertNotNull(model.getMessages());
        assertEquals(1, model.getMessages().size());
        assertEquals("msg-002", model.getMessages().get(0).getId());
        assertEquals("Agent reply", model.getMessages().get(0).getMessage());
        assertEquals("AGENT", model.getMessages().get(0).getSenderType());
    }

    @Test
    public void testRoundTripModelToEntityToModel() {
        SupportTicket original = createSampleModel();

        SupportTicketEntity entity = mapper.toEntity(original);
        SupportTicket roundTripped = mapper.toModel(entity);

        assertEquals(original.getId(), roundTripped.getId());
        assertEquals(original.getCustomerId(), roundTripped.getCustomerId());
        assertEquals(original.getOrderId(), roundTripped.getOrderId());
        assertEquals(original.getSubject(), roundTripped.getSubject());
        assertEquals(original.getCategory(), roundTripped.getCategory());
        assertEquals(original.getPriority(), roundTripped.getPriority());
        assertEquals(original.getDescription(), roundTripped.getDescription());
        assertEquals(original.getAssignedAgentId(), roundTripped.getAssignedAgentId());
        assertEquals(original.getReopenCount(), roundTripped.getReopenCount());
        assertEquals(original.isSlaBreached(), roundTripped.isSlaBreached());
        assertEquals(original.isAutoCloseReady(), roundTripped.isAutoCloseReady());
        assertEquals(original.getMessages().size(), roundTripped.getMessages().size());
    }

    @Test
    public void testNullHandling() {
        assertNull(mapper.toModel(null));
        assertNull(mapper.toEntity(null));
        assertNull(mapper.toMessageModel(null));
        assertNull(mapper.toMessageEntity(null));
    }

    @Test
    public void testMessageModelToEntity() {
        TicketMessage msg = new TicketMessage();
        msg.setId("msg-100");
        msg.setSenderId("sender-1");
        msg.setSenderType("SYSTEM");
        msg.setMessage("System notification");
        msg.setTimestamp(new Date());
        msg.setAttachments(List.of("file1.pdf", "file2.png"));

        TicketMessageEntity entity = mapper.toMessageEntity(msg);

        assertEquals("msg-100", entity.getId());
        assertEquals("sender-1", entity.getSenderId());
        assertEquals("SYSTEM", entity.getSenderType());
        assertEquals("System notification", entity.getMessage());
        assertNotNull(entity.getTimestamp());
        assertNotNull(entity.getAttachments());
        assertTrue(entity.getAttachments().contains("file1.pdf"));
    }

    @Test
    public void testMessageEntityToModel() {
        TicketMessageEntity entity = new TicketMessageEntity();
        entity.setId("msg-200");
        entity.setSenderId("agent-5");
        entity.setSenderType("AGENT");
        entity.setMessage("Working on it");
        entity.setTimestamp(new Date());
        entity.setAttachments("[\"doc.pdf\"]");

        TicketMessage model = mapper.toMessageModel(entity);

        assertEquals("msg-200", model.getId());
        assertEquals("agent-5", model.getSenderId());
        assertEquals("AGENT", model.getSenderType());
        assertEquals("Working on it", model.getMessage());
        assertNotNull(model.getTimestamp());
        assertNotNull(model.getAttachments());
        assertEquals(1, model.getAttachments().size());
        assertEquals("doc.pdf", model.getAttachments().get(0));
    }

    // --- Helper methods ---

    private SupportTicket createSampleModel() {
        SupportTicket model = new SupportTicket();
        model.setId("ticket-001");
        model.setCustomerId("cust-001");
        model.setOrderId("order-123");
        model.setSubject("Test Subject");
        model.setCategory("ORDER");
        model.setPriority("HIGH");
        model.setDescription("Test description");
        model.setAssignedAgentId("agent-001");
        model.setResolvedAt(new Date());
        model.setReopenCount(2);
        model.setSlaBreached(false);
        model.setAutoCloseReady(false);

        TicketMessage msg = new TicketMessage();
        msg.setId("msg-001");
        msg.setSenderId("cust-001");
        msg.setSenderType("CUSTOMER");
        msg.setMessage("Hello");
        msg.setTimestamp(new Date());
        model.setMessages(new ArrayList<>(List.of(msg)));

        return model;
    }

    private SupportTicketEntity createSampleEntity() {
        SupportTicketEntity entity = new SupportTicketEntity();
        entity.setId("ticket-002");
        entity.setCustomerId("cust-002");
        entity.setOrderId("order-456");
        entity.setSubject("Entity Subject");
        entity.setCategory("PAYMENT");
        entity.setPriority("URGENT");
        entity.setDescription("Entity description");
        entity.setAssignedAgentId("agent-002");
        entity.setResolvedAt(new Date());
        entity.setReopenCount(1);
        entity.setSlaBreached(true);
        entity.setAutoCloseReady(false);

        TicketMessageEntity msg = new TicketMessageEntity();
        msg.setId("msg-002");
        msg.setSenderId("agent-002");
        msg.setSenderType("AGENT");
        msg.setMessage("Agent reply");
        msg.setTimestamp(new Date());
        entity.setMessages(new ArrayList<>(List.of(msg)));

        return entity;
    }
}
