package com.homebase.ecom.inventory.service.listener;

import com.ecommerce.inventory.domain.InventoryItem;
import com.ecommerce.inventory.repository.InventoryRepository;
import com.ecommerce.shared.event.KafkaTopics;
import com.ecommerce.shared.event.ProductCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryProductListener {

    private static final Logger log = LoggerFactory.getLogger(InventoryProductListener.class);
    private final InventoryRepository inventoryRepository;
    private final org.springframework.kafka.core.KafkaTemplate<String, Object> kafkaTemplate;

    public InventoryProductListener(InventoryRepository inventoryRepository,
            org.springframework.kafka.core.KafkaTemplate<String, Object> kafkaTemplate) {
        this.inventoryRepository = inventoryRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = KafkaTopics.PRODUCT_EVENTS, groupId = "inventory-group")
    public void onProductEvent(ProductCreatedEvent event) {
        if (!ProductCreatedEvent.EVENT_TYPE.equals(event.getEventType())) {
            return;
        }
        log.info("Inventory: received ProductCreatedEvent for product: {}", event.getProductId());

        if (inventoryRepository.findByProductId(event.getProductId()).isPresent()) {
            log.warn("Inventory item already exists for product: {}", event.getProductId());
            return;
        }

        InventoryItem item = new InventoryItem();
        item.setProductId(event.getProductId());
        item.setQuantity(event.getInitialQuantity());
        item.setReserved(0);
        item.setLowStockThreshold(10); // Default threshold

        inventoryRepository.save(item);
        log.info("Initialized inventory for product: {} with quantity: {}",
                event.getProductId(), event.getInitialQuantity());

        // Notify Saga completion for Product creation
        com.ecommerce.shared.event.ProductInventoryInitializedEvent completionEvent = new com.ecommerce.shared.event.ProductInventoryInitializedEvent(
                event.getProductId(),
                java.time.LocalDateTime.now());

        kafkaTemplate.send(KafkaTopics.PRODUCT_EVENTS,
                event.getProductId(),
                completionEvent);
    }
}
