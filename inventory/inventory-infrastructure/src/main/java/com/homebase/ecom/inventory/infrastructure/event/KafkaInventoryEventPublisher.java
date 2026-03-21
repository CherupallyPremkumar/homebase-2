package com.homebase.ecom.inventory.infrastructure.event;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.domain.port.InventoryEventPublisherPort;
import com.homebase.ecom.shared.event.DamageDetectedEvent;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.LowStockAlertEvent;
import com.homebase.ecom.shared.event.RestockArrivedEvent;
import com.homebase.ecom.shared.event.StockDepletedEvent;
import com.homebase.ecom.shared.event.StockDiscardedEvent;
import com.homebase.ecom.shared.event.StockRejectedEvent;
import com.homebase.ecom.shared.event.StockReturnedToSupplierEvent;
import org.chenile.pubsub.ChenilePub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * Kafka-backed implementation of InventoryEventPublisherPort.
 * Translates domain method calls into Kafka messages via ChenilePub.
 * No @Component -- wired explicitly via @Bean in InventoryInfrastructureConfiguration.
 */
public class KafkaInventoryEventPublisher implements InventoryEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaInventoryEventPublisher.class);

    private final ChenilePub chenilePub;
    private final ObjectMapper objectMapper;

    public KafkaInventoryEventPublisher(ChenilePub chenilePub, ObjectMapper objectMapper) {
        this.chenilePub = chenilePub;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publishDamageDetected(InventoryItem item, int damagedQuantity, boolean severeDamage) {
        DamageDetectedEvent event = new DamageDetectedEvent(
                item.getId(), item.getProductId(), damagedQuantity,
                item.getDamagePercentage(), severeDamage);
        publish(event, DamageDetectedEvent.EVENT_TYPE, item,
                "DamageDetectedEvent", "damagedQty=" + damagedQuantity + ", severe=" + severeDamage);
    }

    @Override
    public void publishStockRejected(InventoryItem item, String reason) {
        StockRejectedEvent event = new StockRejectedEvent(
                item.getId(), item.getProductId(), reason);
        publish(event, StockRejectedEvent.EVENT_TYPE, item,
                "StockRejectedEvent", "reason=" + reason);
    }

    @Override
    public void publishLowStockAlert(InventoryItem item) {
        LowStockAlertEvent event = new LowStockAlertEvent(
                item.getId(), item.getProductId(),
                item.getAvailableQuantity(), item.getLowStockThreshold());
        publish(event, LowStockAlertEvent.EVENT_TYPE, item,
                "LowStockAlertEvent", "available=" + item.getAvailableQuantity());
    }

    @Override
    public void publishStockDiscarded(InventoryItem item) {
        StockDiscardedEvent event = new StockDiscardedEvent(
                item.getId(), item.getProductId(), item.getQuantity());
        publish(event, StockDiscardedEvent.EVENT_TYPE, item,
                "StockDiscardedEvent", "qty=" + item.getQuantity());
    }

    @Override
    public void publishRestockArrived(InventoryItem item, int restockQuantity) {
        RestockArrivedEvent event = new RestockArrivedEvent(
                item.getId(), item.getProductId(), restockQuantity);
        publish(event, RestockArrivedEvent.EVENT_TYPE, item,
                "RestockArrivedEvent", "qty=" + restockQuantity);
    }

    @Override
    public void publishStockDepleted(InventoryItem item) {
        StockDepletedEvent event = new StockDepletedEvent(
                item.getId(), item.getProductId(), item.getVariantId());
        publish(event, StockDepletedEvent.EVENT_TYPE, item,
                "StockDepletedEvent", null);
    }

    @Override
    public void publishStockReturnedToSupplier(InventoryItem item) {
        StockReturnedToSupplierEvent event = new StockReturnedToSupplierEvent(
                item.getId(), item.getProductId(), item.getQuantity());
        publish(event, StockReturnedToSupplierEvent.EVENT_TYPE, item,
                "StockReturnedToSupplierEvent", "qty=" + item.getQuantity());
    }

    private void publish(Object event, String eventType, InventoryItem item,
                         String eventName, String extraInfo) {
        try {
            String body = objectMapper.writeValueAsString(event);
            String key = item.getProductId() != null ? item.getProductId() : item.getId();
            chenilePub.publish(KafkaTopics.INVENTORY_EVENTS, body,
                    Map.of("key", key, "eventType", eventType));
            if (extraInfo != null) {
                log.info("Published {} for productId={}, {}", eventName, item.getProductId(), extraInfo);
            } else {
                log.info("Published {} for productId={}", eventName, item.getProductId());
            }
        } catch (JacksonException e) {
            log.error("Failed to serialize {} for productId={}", eventName, item.getProductId(), e);
        }
    }
}
