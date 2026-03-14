package com.homebase.ecom.inventory.service.listener;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.domain.port.InventoryItemRepository;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.ProductCreatedEvent;
import com.homebase.ecom.shared.event.ProductInventoryInitializedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryProductListener {

    private static final Logger log = LoggerFactory.getLogger(InventoryProductListener.class);
    private final InventoryItemRepository inventoryItemRepository;
    private final org.springframework.kafka.core.KafkaTemplate<String, Object> kafkaTemplate;

    public InventoryProductListener(InventoryItemRepository inventoryItemRepository,
            org.springframework.kafka.core.KafkaTemplate<String, Object> kafkaTemplate) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = KafkaTopics.PRODUCT_EVENTS, groupId = "inventory-group")
    public void onProductEvent(ProductCreatedEvent event) {
        if (!ProductCreatedEvent.EVENT_TYPE.equals(event.getEventType())) {
            return;
        }
        log.info("Inventory: received ProductCreatedEvent for product: {}", event.getProductId());

        if (inventoryItemRepository.findByProductId(event.getProductId()).isPresent()) {
            log.warn("Inventory item already exists for product: {}", event.getProductId());
            return;
        }

        InventoryItem item = new InventoryItem();
        item.setProductId(event.getProductId());
        item.setQuantity(event.getInitialQuantity());
        item.setReservedQuantity(0);
        item.setLowStockThreshold(10); // Default threshold

        inventoryItemRepository.save(item);
        log.info("Initialized inventory for product: {} with quantity: {}",
                event.getProductId(), event.getInitialQuantity());

        // Notify Saga completion for Product creation
        ProductInventoryInitializedEvent completionEvent = new ProductInventoryInitializedEvent(
                event.getProductId(),
                java.time.LocalDateTime.now());

        kafkaTemplate.send(KafkaTopics.PRODUCT_EVENTS,
                event.getProductId(),
                completionEvent);
    }
}
