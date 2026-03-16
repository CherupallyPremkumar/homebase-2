package com.homebase.ecom.order.infrastructure.persistence.mapper;

import com.homebase.ecom.order.infrastructure.persistence.entity.OrderEntity;
import com.homebase.ecom.order.infrastructure.persistence.entity.OrderItemEntity;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.model.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for OrderMapper bidirectional mapping.
 */
class OrderMapperTest {

    private OrderMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new OrderMapper();
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
    void toModel_mapsAllFields() {
        OrderEntity entity = new OrderEntity();
        entity.setId("ord-001");
        entity.setOrderNumber("ORD-12345");
        entity.setCustomerId("cust-001");
        entity.setSubtotal(new BigDecimal("1000.00"));
        entity.setTaxAmount(new BigDecimal("100.00"));
        entity.setShippingAmount(new BigDecimal("50.00"));
        entity.setTotalAmount(new BigDecimal("1150.00"));
        entity.setCurrency("INR");
        entity.setShippingAddressId("addr-ship-001");
        entity.setBillingAddressId("addr-bill-001");
        entity.setPaymentMethodId("pm-001");
        entity.setNotes("Handle with care");
        entity.setCancelReason("Customer request");
        entity.setDescription("Test order");

        Order model = mapper.toModel(entity);

        assertEquals("ord-001", model.getId());
        assertEquals("ORD-12345", model.getOrderNumber());
        assertEquals("cust-001", model.getCustomerId());
        assertEquals(new BigDecimal("1000.00"), model.getSubtotal());
        assertEquals(new BigDecimal("100.00"), model.getTaxAmount());
        assertEquals(new BigDecimal("50.00"), model.getShippingAmount());
        assertEquals(new BigDecimal("1150.00"), model.getTotalAmount());
        assertEquals("INR", model.getCurrency());
        assertEquals("addr-ship-001", model.getShippingAddressId());
        assertEquals("addr-bill-001", model.getBillingAddressId());
        assertEquals("pm-001", model.getPaymentMethodId());
        assertEquals("Handle with care", model.getNotes());
        assertEquals("Customer request", model.getCancelReason());
        assertEquals("Test order", model.description);
    }

    @Test
    void toEntity_mapsAllFields() {
        Order model = new Order();
        model.setId("ord-002");
        model.setOrderNumber("ORD-67890");
        model.setCustomerId("cust-002");
        model.setSubtotal(new BigDecimal("2000.00"));
        model.setTaxAmount(new BigDecimal("200.00"));
        model.setShippingAmount(new BigDecimal("100.00"));
        model.setTotalAmount(new BigDecimal("2300.00"));
        model.setCurrency("USD");
        model.setShippingAddressId("addr-ship-002");
        model.setBillingAddressId("addr-bill-002");
        model.setPaymentMethodId("pm-002");
        model.setNotes("Fragile");
        model.setCancelReason(null);
        model.description = "Another order";

        OrderEntity entity = mapper.toEntity(model);

        assertEquals("ord-002", entity.getId());
        assertEquals("ORD-67890", entity.getOrderNumber());
        assertEquals("cust-002", entity.getCustomerId());
        assertEquals(new BigDecimal("2000.00"), entity.getSubtotal());
        assertEquals(new BigDecimal("200.00"), entity.getTaxAmount());
        assertEquals(new BigDecimal("100.00"), entity.getShippingAmount());
        assertEquals(new BigDecimal("2300.00"), entity.getTotalAmount());
        assertEquals("USD", entity.getCurrency());
        assertEquals("addr-ship-002", entity.getShippingAddressId());
        assertEquals("addr-bill-002", entity.getBillingAddressId());
        assertEquals("pm-002", entity.getPaymentMethodId());
        assertEquals("Fragile", entity.getNotes());
        assertNull(entity.getCancelReason());
        assertEquals("Another order", entity.getDescription());
    }

    @Test
    void toItemModel_mapsAllFields() {
        OrderItemEntity entity = new OrderItemEntity();
        entity.setId("item-001");
        entity.setProductId("prod-001");
        entity.setVariantId("var-001");
        entity.setSku("SKU-001");
        entity.setProductName("Test Product");
        entity.setQuantity(3);
        entity.setUnitPrice(new BigDecimal("500.00"));
        entity.setTotalPrice(new BigDecimal("1500.00"));

        OrderItem model = mapper.toItemModel(entity);

        assertEquals("item-001", model.getId());
        assertEquals("prod-001", model.getProductId());
        assertEquals("var-001", model.getVariantId());
        assertEquals("SKU-001", model.getSku());
        assertEquals("Test Product", model.getProductName());
        assertEquals(3, model.getQuantity());
        assertEquals(new BigDecimal("500.00"), model.getUnitPrice());
        assertEquals(new BigDecimal("1500.00"), model.getTotalPrice());
    }

    @Test
    void toItemEntity_mapsAllFields() {
        OrderItem model = new OrderItem();
        model.setId("item-002");
        model.setProductId("prod-002");
        model.setVariantId("var-002");
        model.setSku("SKU-002");
        model.setProductName("Another Product");
        model.setQuantity(1);
        model.setUnitPrice(new BigDecimal("800.00"));
        model.setTotalPrice(new BigDecimal("800.00"));

        OrderItemEntity entity = mapper.toItemEntity(model);

        assertEquals("item-002", entity.getId());
        assertEquals("prod-002", entity.getProductId());
        assertEquals("var-002", entity.getVariantId());
        assertEquals("SKU-002", entity.getSku());
        assertEquals("Another Product", entity.getProductName());
        assertEquals(1, entity.getQuantity());
        assertEquals(new BigDecimal("800.00"), entity.getUnitPrice());
        assertEquals(new BigDecimal("800.00"), entity.getTotalPrice());
    }

    @Test
    void roundTrip_orderWithItems() {
        Order original = new Order();
        original.setId("ord-rt");
        original.setOrderNumber("ORD-RT-001");
        original.setCustomerId("cust-rt");
        original.setTotalAmount(new BigDecimal("1500.00"));
        original.setCurrency("INR");
        original.description = "Round trip test";

        OrderItem item = new OrderItem();
        item.setId("item-rt");
        item.setProductId("prod-rt");
        item.setProductName("RT Product");
        item.setQuantity(2);
        item.setUnitPrice(new BigDecimal("750.00"));
        item.setTotalPrice(new BigDecimal("1500.00"));
        original.setItems(List.of(item));

        // model -> entity -> model
        OrderEntity entity = mapper.toEntity(original);
        Order roundTripped = mapper.toModel(entity);

        assertEquals(original.getId(), roundTripped.getId());
        assertEquals(original.getOrderNumber(), roundTripped.getOrderNumber());
        assertEquals(original.getCustomerId(), roundTripped.getCustomerId());
        assertEquals(original.getTotalAmount(), roundTripped.getTotalAmount());
        assertEquals(original.getCurrency(), roundTripped.getCurrency());
        assertEquals(original.description, roundTripped.description);
        assertEquals(1, roundTripped.getItems().size());
        assertEquals("prod-rt", roundTripped.getItems().get(0).getProductId());
        assertEquals(2, roundTripped.getItems().get(0).getQuantity());
    }
}
