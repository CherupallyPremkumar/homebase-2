package com.homebase.ecom.inventory.service.event;

import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.infrastructure.persistence.adapter.InventoryItemQueryAdapter;
import com.homebase.ecom.inventory.service.InventoryService;
import com.homebase.ecom.shared.event.*;
import org.chenile.pubsub.ChenilePub;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for InventoryEventHandler — verifies cross-service Kafka event
 * processing without requiring a running Kafka broker or Spring context.
 *
 * Tests the product-to-inventory event flow (ProductCreatedEvent) and
 * order-to-inventory event flow (OrderCreatedEvent) by mocking the STM service
 * and other dependencies, then calling handler methods directly.
 */
@ExtendWith(MockitoExtension.class)
class InventoryEventHandlerTest {

    @Mock
    private InventoryService inventoryService;

    @Mock
    private InventoryItemQueryAdapter inventoryItemQueryAdapter;

    @Mock
    private StateEntityServiceImpl<InventoryItem> inventoryStateEntityService;

    @Mock
    private ChenilePub chenilePub;

    private ObjectMapper objectMapper;

    private InventoryEventHandler handler;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        handler = new InventoryEventHandler(
                inventoryService,
                inventoryItemQueryAdapter,
                inventoryStateEntityService,
                chenilePub,
                objectMapper);
    }

    // =========================================================================
    // product.events -> Inventory: ProductCreatedEvent (happy path)
    // =========================================================================

    @Nested
    @DisplayName("handleProductEvent — ProductCreatedEvent")
    class HandleProductCreatedEvent {

        @Test
        @DisplayName("should initialize inventory item via STM when product is created")
        void shouldInitializeInventoryWhenProductCreated() {
            // Given: a new product created event from the product service
            ProductCreatedEvent event = new ProductCreatedEvent(
                    "PROD-001", "Wireless Mouse", 100, LocalDateTime.now());

            // No existing inventory for this product
            when(inventoryItemQueryAdapter.findByProductId("PROD-001")).thenReturn(null);

            // When: the event handler processes the ProductCreatedEvent
            handler.handleProductEvent(event);

            // Then: STM process is called with a new InventoryItem
            ArgumentCaptor<InventoryItem> itemCaptor = ArgumentCaptor.forClass(InventoryItem.class);
            verify(inventoryStateEntityService).process(itemCaptor.capture(), isNull(), isNull());

            InventoryItem createdItem = itemCaptor.getValue();
            assertEquals("PROD-001", createdItem.getProductId(),
                    "Inventory item should reference the product ID from the event");
            assertEquals(100, createdItem.getQuantity(),
                    "Initial quantity should match the event's initialQuantity");
            assertEquals(0, createdItem.getReservedQuantity(),
                    "Reserved quantity should start at zero");
            assertEquals(10, createdItem.getLowStockThreshold(),
                    "Low stock threshold should default to 10");

            // Then: a completion event is published back to product.events
            verify(chenilePub).publish(
                    eq(KafkaTopics.PRODUCT_EVENTS),
                    anyString(),
                    anyMap());
        }

        @Test
        @DisplayName("should skip creation if inventory item already exists for product")
        void shouldSkipWhenInventoryAlreadyExists() {
            // Given: product event for a product that already has inventory
            ProductCreatedEvent event = new ProductCreatedEvent(
                    "PROD-002", "Keyboard", 50, LocalDateTime.now());

            InventoryItem existingItem = new InventoryItem();
            existingItem.setProductId("PROD-002");
            when(inventoryItemQueryAdapter.findByProductId("PROD-002")).thenReturn(existingItem);

            // When
            handler.handleProductEvent(event);

            // Then: STM process should NOT be called (idempotent — no duplicate inventory)
            verify(inventoryStateEntityService, never()).process(any(), any(), any());
            verify(chenilePub, never()).publish(anyString(), anyString(), anyMap());
        }

        @Test
        @DisplayName("should ignore event with wrong event type")
        void shouldIgnoreWrongEventType() {
            // Given: an event object with a non-matching eventType
            ProductCreatedEvent event = new ProductCreatedEvent(
                    "PROD-003", "Monitor", 25, LocalDateTime.now());
            event.setEventType("SOME_OTHER_EVENT");

            // When
            handler.handleProductEvent(event);

            // Then: nothing should happen
            verify(inventoryItemQueryAdapter, never()).findByProductId(anyString());
            verify(inventoryStateEntityService, never()).process(any(), any(), any());
        }

        @Test
        @DisplayName("should publish ProductInventoryInitializedEvent after successful creation")
        void shouldPublishCompletionEvent() {
            // Given
            ProductCreatedEvent event = new ProductCreatedEvent(
                    "PROD-004", "USB Cable", 200, LocalDateTime.now());
            when(inventoryItemQueryAdapter.findByProductId("PROD-004")).thenReturn(null);

            // When
            handler.handleProductEvent(event);

            // Then: verify the published event contains the correct product ID and topic
            ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<Map> headersCaptor = ArgumentCaptor.forClass(Map.class);

            verify(chenilePub).publish(topicCaptor.capture(), bodyCaptor.capture(), headersCaptor.capture());

            assertEquals(KafkaTopics.PRODUCT_EVENTS, topicCaptor.getValue(),
                    "Completion event should be published to product.events topic");

            String publishedBody = bodyCaptor.getValue();
            assertTrue(publishedBody.contains("PROD-004"),
                    "Published event body should contain the product ID");
            assertTrue(publishedBody.contains(ProductInventoryInitializedEvent.EVENT_TYPE),
                    "Published event body should contain the PRODUCT_INVENTORY_INITIALIZED event type");

            Map<String, ?> headers = headersCaptor.getValue();
            assertEquals("PROD-004", headers.get("key"),
                    "Kafka message key should be the product ID");
            assertEquals(ProductInventoryInitializedEvent.EVENT_TYPE, headers.get("eventType"),
                    "Header should include the event type for routing");
        }
    }

    // =========================================================================
    // order.events -> Inventory: OrderCreatedEvent (happy path)
    // =========================================================================

    @Nested
    @DisplayName("handleOrderEvent — OrderCreatedEvent")
    class HandleOrderCreatedEvent {

        @Test
        @DisplayName("should reserve stock when order is created")
        void shouldReserveStockOnOrderCreated() throws Exception {
            // Given: an order with two line items
            OrderCreatedEvent event = new OrderCreatedEvent();
            event.setOrderId("ORD-001");
            event.setEventType(OrderCreatedEvent.EVENT_TYPE);
            event.setItems(List.of(
                    new OrderCreatedEvent.OrderItemPayload("PROD-001", 2),
                    new OrderCreatedEvent.OrderItemPayload("PROD-002", 3)));

            // When
            handler.handleOrderEvent(event);

            // Then: reserveForOrder should be called with the correct items map
            @SuppressWarnings("unchecked")
            ArgumentCaptor<Map<String, Integer>> itemsCaptor =
                    ArgumentCaptor.forClass(Map.class);
            verify(inventoryService).reserveForOrder(eq("ORD-001"), itemsCaptor.capture());

            Map<String, Integer> reservedItems = itemsCaptor.getValue();
            assertEquals(2, reservedItems.get("PROD-001"));
            assertEquals(3, reservedItems.get("PROD-002"));

            // Then: a STOCK_RESERVED event should be published (not in transaction, so immediate)
            // Since TransactionSynchronizationManager is not active in unit test, publishAfterCommit
            // falls through to immediate publish
            verify(chenilePub).publish(
                    eq(KafkaTopics.INVENTORY_EVENTS),
                    anyString(),
                    anyMap());
        }

        @Test
        @DisplayName("should publish STOCK_FAILED when reservation throws exception")
        void shouldPublishStockFailedOnError() throws Exception {
            // Given
            OrderCreatedEvent event = new OrderCreatedEvent();
            event.setOrderId("ORD-002");
            event.setEventType(OrderCreatedEvent.EVENT_TYPE);
            event.setItems(List.of(new OrderCreatedEvent.OrderItemPayload("PROD-001", 999)));

            doThrow(new RuntimeException("Insufficient stock"))
                    .when(inventoryService).reserveForOrder(eq("ORD-002"), anyMap());

            // When
            handler.handleOrderEvent(event);

            // Then: STOCK_FAILED event should be published
            ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
            verify(chenilePub).publish(
                    eq(KafkaTopics.INVENTORY_EVENTS),
                    bodyCaptor.capture(),
                    anyMap());

            String body = bodyCaptor.getValue();
            assertTrue(body.contains("ORD-002"), "Event body should contain the order ID");
            assertTrue(body.contains(InventoryEvent.STOCK_FAILED),
                    "Event body should contain STOCK_FAILED event type");
        }

        @Test
        @DisplayName("should ignore event with wrong event type")
        void shouldIgnoreNonOrderCreatedEvent() throws Exception {
            // Given
            OrderCreatedEvent event = new OrderCreatedEvent();
            event.setOrderId("ORD-003");
            event.setEventType("SOME_OTHER_TYPE");

            // When
            handler.handleOrderEvent(event);

            // Then
            verify(inventoryService, never()).reserveForOrder(anyString(), anyMap());
        }
    }

    // =========================================================================
    // payment.events -> Inventory: PaymentSucceededEvent / PaymentFailedEvent
    // =========================================================================

    @Nested
    @DisplayName("handlePaymentEvent — payment lifecycle")
    class HandlePaymentEvent {

        @Test
        @DisplayName("should commit stock on payment success")
        void shouldCommitOnPaymentSuccess() {
            // Given
            PaymentSucceededEvent paymentEvent = new PaymentSucceededEvent();
            paymentEvent.setOrderId("ORD-010");

            EventEnvelope envelope = EventEnvelope.of(
                    PaymentSucceededEvent.EVENT_TYPE, 1, paymentEvent);

            // When
            handler.handlePaymentEvent(envelope);

            // Then
            verify(inventoryService).commit("ORD-010");
        }

        @Test
        @DisplayName("should release stock on payment failure")
        void shouldReleaseOnPaymentFailure() {
            // Given
            PaymentFailedEvent paymentEvent = new PaymentFailedEvent();
            paymentEvent.setOrderId("ORD-011");

            EventEnvelope envelope = EventEnvelope.of(
                    PaymentFailedEvent.EVENT_TYPE, 1, paymentEvent);

            // When
            handler.handlePaymentEvent(envelope);

            // Then
            verify(inventoryService).release("ORD-011");
        }

        @Test
        @DisplayName("should release stock on payment session expired")
        void shouldReleaseOnPaymentExpired() {
            // Given
            PaymentSessionExpiredEvent expiredEvent = new PaymentSessionExpiredEvent();
            expiredEvent.setOrderId("ORD-012");

            EventEnvelope envelope = EventEnvelope.of(
                    PaymentSessionExpiredEvent.EVENT_TYPE, 1, expiredEvent);

            // When
            handler.handlePaymentEvent(envelope);

            // Then
            verify(inventoryService).release("ORD-012");
        }

        @Test
        @DisplayName("should ignore null envelope")
        void shouldIgnoreNullEnvelope() {
            // When
            handler.handlePaymentEvent(null);

            // Then
            verifyNoInteractions(inventoryService);
        }

        @Test
        @DisplayName("should ignore unknown payment event type")
        void shouldIgnoreUnknownPaymentEventType() {
            // Given
            EventEnvelope envelope = EventEnvelope.of("UNKNOWN_PAYMENT_EVENT", 1, Map.of());

            // When
            handler.handlePaymentEvent(envelope);

            // Then
            verifyNoInteractions(inventoryService);
        }
    }

    // =========================================================================
    // inventory.events -> Inventory: InventoryEvent (self-consumption)
    // =========================================================================

    @Nested
    @DisplayName("handleInventoryEvent — release stock command")
    class HandleInventoryEvent {

        @Test
        @DisplayName("should release stock and publish STOCK_RELEASED on RELEASE_STOCK command")
        void shouldReleaseAndPublish() {
            // Given
            InventoryEvent event = new InventoryEvent("ORD-020", InventoryEvent.RELEASE_STOCK);

            // When
            handler.handleInventoryEvent(event);

            // Then
            verify(inventoryService).release("ORD-020");
            // publishAfterCommit falls through to immediate publish outside transaction
            verify(chenilePub).publish(
                    eq(KafkaTopics.INVENTORY_EVENTS),
                    anyString(),
                    anyMap());
        }

        @Test
        @DisplayName("should ignore non-RELEASE_STOCK event types")
        void shouldIgnoreOtherInventoryEvents() {
            // Given
            InventoryEvent event = new InventoryEvent("ORD-021", "SOME_OTHER_TYPE");

            // When
            handler.handleInventoryEvent(event);

            // Then
            verifyNoInteractions(inventoryService);
        }
    }
}
