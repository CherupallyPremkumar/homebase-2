package com.homebase.ecom.product.service.event;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.domain.port.ProductRepository;
import com.homebase.ecom.shared.event.EventEnvelope;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.ProductArchivedEvent;
import com.homebase.ecom.shared.event.ProductDisabledEvent;
import com.homebase.ecom.shared.event.ProductDiscontinuedEvent;
import com.homebase.ecom.shared.event.ProductPublishedEvent;
import com.homebase.ecom.shared.event.ProductRecalledEvent;
import com.homebase.ecom.shared.event.ProductUpdatedEvent;
import com.homebase.ecom.shared.event.SupplierBlacklistedEvent;
import com.homebase.ecom.shared.event.SupplierProductReturnedEvent;
import com.homebase.ecom.shared.event.SupplierSuspendedEvent;
import org.chenile.pubsub.ChenilePub;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Chenile-kafka event handler for the Product bounded context.
 *
 * <p><b>Inbound</b> (auto-subscribed via productEventService.json):
 * <ul>
 *   <li>{@code supplier.events} — react to supplier lifecycle changes</li>
 * </ul>
 *
 * <p><b>Outbound</b> (called by PostSaveHooks after state transitions):
 * <ul>
 *   <li>{@code product.events} — publish product lifecycle events for Catalog, Inventory, etc.</li>
 * </ul>
 */
public class ProductEventHandler {

    private static final Logger log = LoggerFactory.getLogger(ProductEventHandler.class);

    private final StateEntityServiceImpl<Product> productStateEntityService;
    private final ProductRepository productRepository;
    private final ChenilePub chenilePub;
    private final ObjectMapper objectMapper;

    public ProductEventHandler(
            @Qualifier("_productStateEntityService_") StateEntityServiceImpl<Product> productStateEntityService,
            ProductRepository productRepository,
            ChenilePub chenilePub,
            ObjectMapper objectMapper) {
        this.productStateEntityService = productStateEntityService;
        this.productRepository = productRepository;
        this.chenilePub = chenilePub;
        this.objectMapper = objectMapper;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // INBOUND: supplier.events
    // ═══════════════════════════════════════════════════════════════════════

    public void handleSupplierEvent(EventEnvelope envelope) {
        if (envelope == null || envelope.getEventType() == null) {
            return;
        }

        switch (envelope.getEventType()) {
            case SupplierProductReturnedEvent.EVENT_TYPE ->
                    onSupplierProductReturned(
                            objectMapper.convertValue(envelope.getPayload(), SupplierProductReturnedEvent.class));

            case SupplierSuspendedEvent.EVENT_TYPE ->
                    onSupplierSuspended(
                            objectMapper.convertValue(envelope.getPayload(), SupplierSuspendedEvent.class));

            case SupplierBlacklistedEvent.EVENT_TYPE ->
                    onSupplierBlacklisted(
                            objectMapper.convertValue(envelope.getPayload(), SupplierBlacklistedEvent.class));

            default -> log.debug("Ignoring unknown supplier event type: {}", envelope.getEventType());
        }
    }

    @Transactional
    private void onSupplierProductReturned(SupplierProductReturnedEvent event) {
        log.info("SupplierProductReturnedEvent — productId={}, supplierId={}, reason={}",
                event.getProductId(), event.getSupplierId(), event.getReason());

        try {
            Map<String, Object> transientProperties = new HashMap<>();
            transientProperties.put("discontinueReason", "Supplier returned product: " + event.getReason());
            productStateEntityService.processById(event.getProductId(), "discontinueProduct", transientProperties);
            log.info("Product {} discontinued due to supplier return.", event.getProductId());
        } catch (RuntimeException e) {
            log.warn("Idempotency: product {} already transitioned past discontinue (possible replay). Skipping. Detail: {}",
                    event.getProductId(), e.getMessage());
        } catch (Exception e) {
            log.error("Failed to discontinue product {} on supplier return.", event.getProductId(), e);
        }
    }

    @Transactional
    private void onSupplierSuspended(SupplierSuspendedEvent event) {
        log.info("SupplierSuspendedEvent — supplierId={}, reason={}",
                event.getSupplierId(), event.getReason());
        // TODO: query all PUBLISHED products by supplierId, disable each via STM
        List<Product> publishedProducts = productRepository.findBySupplierIdAndStateId(
                event.getSupplierId(), "PUBLISHED");
        log.info("Found {} PUBLISHED products for suspended supplier {}",
                publishedProducts.size(), event.getSupplierId());

        for (Product product : publishedProducts) {
            try {
                Map<String, Object> payload = new HashMap<>();
                payload.put("disableReason", "Supplier suspended: " + event.getReason());
                productStateEntityService.processById(product.getId(), "disableProduct", payload);
                log.info("Product {} disabled due to supplier {} suspension.",
                        product.getId(), event.getSupplierId());
            } catch (RuntimeException e) {
                log.warn("Idempotency: product {} already disabled (possible replay). Skipping. Detail: {}",
                        product.getId(), e.getMessage());
            } catch (Exception e) {
                log.error("Failed to disable product {} for suspended supplier {}.",
                        product.getId(), event.getSupplierId(), e);
            }
        }
    }

    @Transactional
    private void onSupplierBlacklisted(SupplierBlacklistedEvent event) {
        log.warn("SupplierBlacklistedEvent — supplierId={}, reason={}",
                event.getSupplierId(), event.getReason());
        // TODO: query all products by supplierId, discontinue each via STM
        List<Product> supplierProducts = productRepository.findBySupplierId(event.getSupplierId());
        log.info("Found {} products for blacklisted supplier {}",
                supplierProducts.size(), event.getSupplierId());

        for (Product product : supplierProducts) {
            String currentState = product.getCurrentState() != null
                    ? product.getCurrentState().getStateId() : null;
            // Skip products already in terminal states
            if ("DISCONTINUED".equals(currentState) || "RECALLED".equals(currentState)) {
                log.debug("Product {} already in state {}, skipping.", product.getId(), currentState);
                continue;
            }
            try {
                Map<String, Object> payload = new HashMap<>();
                payload.put("discontinueReason", "Supplier blacklisted: " + event.getReason());
                productStateEntityService.processById(product.getId(), "discontinueProduct", payload);
                log.info("Product {} discontinued due to supplier {} blacklisting.",
                        product.getId(), event.getSupplierId());
            } catch (RuntimeException e) {
                log.warn("Idempotency: product {} already discontinued (possible replay). Skipping. Detail: {}",
                        product.getId(), e.getMessage());
            } catch (Exception e) {
                log.error("Failed to discontinue product {} for blacklisted supplier {}.",
                        product.getId(), event.getSupplierId(), e);
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // OUTBOUND: product.events (called by PostSaveHooks)
    // ═══════════════════════════════════════════════════════════════════════

    public void publishProductPublished(ProductPublishedEvent event) {
        publishAfterCommit(KafkaTopics.PRODUCT_EVENTS, event.getProductId(), event,
                ProductPublishedEvent.EVENT_TYPE);
    }

    public void publishProductUpdated(ProductUpdatedEvent event) {
        publishAfterCommit(KafkaTopics.PRODUCT_EVENTS, event.getProductId(), event,
                ProductUpdatedEvent.EVENT_TYPE);
    }

    public void publishProductDisabled(ProductDisabledEvent event) {
        publishAfterCommit(KafkaTopics.PRODUCT_EVENTS, event.getProductId(), event,
                ProductDisabledEvent.EVENT_TYPE);
    }

    public void publishProductArchived(ProductArchivedEvent event) {
        publishAfterCommit(KafkaTopics.PRODUCT_EVENTS, event.getProductId(), event,
                ProductArchivedEvent.EVENT_TYPE);
    }

    public void publishProductRecalled(ProductRecalledEvent event) {
        publishAfterCommit(KafkaTopics.PRODUCT_EVENTS, event.getProductId(), event,
                ProductRecalledEvent.EVENT_TYPE);
    }

    public void publishProductDiscontinued(ProductDiscontinuedEvent event) {
        publishAfterCommit(KafkaTopics.PRODUCT_EVENTS, event.getProductId(), event,
                ProductDiscontinuedEvent.EVENT_TYPE);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // INTERNAL: Kafka publish via ChenilePub
    // ═══════════════════════════════════════════════════════════════════════

    private void publishAfterCommit(String topic, String key, Object payload, String eventType) {
        Runnable action = () -> doPublish(topic, key, payload, eventType);

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    action.run();
                }
            });
        } else {
            action.run();
        }
    }

    private void doPublish(String topic, String key, Object payload, String eventType) {
        try {
            String body = objectMapper.writeValueAsString(payload);
            Map<String, Object> headers = new HashMap<>();
            headers.put("key", key);
            headers.put("eventType", eventType);
            chenilePub.publish(topic, body, headers);
            log.info("Published {} to {} for key={}", eventType, topic, key);
        } catch (JacksonException e) {
            log.error("Failed to serialize {} for topic={}, key={}", eventType, topic, key, e);
        }
    }
}
