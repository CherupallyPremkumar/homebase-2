package com.homebase.ecom.product.service.event;

import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.ProductDisabledEvent;
import com.homebase.ecom.shared.event.ProductDiscontinuedEvent;
import com.homebase.ecom.shared.event.ProductPublishedEvent;
import com.homebase.ecom.shared.event.ProductStockUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Publishes product lifecycle events to Kafka.
 * Follows the same pattern as OrderEventPublisher -- sends after transaction commit.
 */
@Component
public class ProductEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(ProductEventPublisher.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ProductEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Publishes when a product reaches the PUBLISHED state.
     * The Catalog module consumes this to create/update a CatalogItem.
     */
    public void publishProductPublished(ProductPublishedEvent event) {
        log.info("Scheduling ProductPublishedEvent publish after commit for product: {}", event.getProductId());
        sendAfterCommit(KafkaTopics.PRODUCT_EVENTS, event.getProductId(), event);
    }

    /**
     * Publishes when a product is disabled (temporarily hidden from catalog).
     * The Catalog module consumes this to hide the CatalogItem.
     */
    public void publishProductDisabled(ProductDisabledEvent event) {
        log.info("Scheduling ProductDisabledEvent publish after commit for product: {}", event.getProductId());
        sendAfterCommit(KafkaTopics.PRODUCT_EVENTS, event.getProductId(), event);
    }

    /**
     * Publishes when a product is permanently discontinued.
     * The Catalog module consumes this to remove the CatalogItem permanently.
     */
    public void publishProductDiscontinued(ProductDiscontinuedEvent event) {
        log.info("Scheduling ProductDiscontinuedEvent publish after commit for product: {}", event.getProductId());
        sendAfterCommit(KafkaTopics.PRODUCT_EVENTS, event.getProductId(), event);
    }

    /**
     * Publishes when product stock levels change.
     * The Catalog module consumes this to update visibility.
     */
    public void publishStockUpdated(ProductStockUpdatedEvent event) {
        log.info("Scheduling ProductStockUpdatedEvent publish after commit for product: {}", event.getProductId());
        sendAfterCommit(KafkaTopics.PRODUCT_EVENTS, event.getProductId(), event);
    }

    private void sendAfterCommit(String topic, String key, Object payload) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    kafkaTemplate.send(topic, key, payload);
                }
            });
        } else {
            kafkaTemplate.send(topic, key, payload);
        }
    }
}
