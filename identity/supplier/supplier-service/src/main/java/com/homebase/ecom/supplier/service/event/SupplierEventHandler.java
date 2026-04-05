package com.homebase.ecom.supplier.service.event;

import com.homebase.ecom.shared.event.EventEnvelope;
import com.homebase.ecom.supplier.model.Supplier;
import com.homebase.ecom.supplier.service.validator.SupplierPolicyValidator;
import org.chenile.utils.entity.service.EntityStore;
import org.chenile.stm.STM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import tools.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * Chenile event handler for supplier cross-service events.
 * Registered via supplierEventService.json -- operations subscribe to Kafka topics
 * through chenile-kafka auto-subscription (CustomKafkaConsumer + EventProcessor).
 *
 * CONSUMES:
 *   order.events  -> ORDER_COMPLETED -> update totalOrders, fulfillmentRate
 *   review.events -> REVIEW_PUBLISHED -> update rating
 *   return.events -> RETURN_COMPLETED -> update totalReturns, return rate
 *
 * Bean name "supplierEventService" must match the service JSON id.
 */
public class SupplierEventHandler {

    private static final Logger log = LoggerFactory.getLogger(SupplierEventHandler.class);

    private final EntityStore<Supplier> entityStore;
    private final STM<Supplier> stm;
    private final ObjectMapper objectMapper;

    @Autowired(required = false)
    private SupplierPolicyValidator policyValidator;

    public SupplierEventHandler(
            @Qualifier("supplierEntityStore") EntityStore<Supplier> entityStore,
            @Qualifier("supplierEntityStm") STM<Supplier> stm,
            ObjectMapper objectMapper) {
        this.entityStore = entityStore;
        this.stm = stm;
        this.objectMapper = objectMapper;
    }

    // -- order.events -------------------------------------------------------

    /**
     * Handles ORDER_COMPLETED events.
     * Increments totalOrders and recalculates fulfillmentRate for the supplier.
     */
    @Transactional
    public void handleOrderEvent(EventEnvelope envelope) {
        if (envelope == null || envelope.getEventType() == null) {
            return;
        }

        if (!"ORDER_COMPLETED".equals(envelope.getEventType())) {
            return;
        }

        try {
            Map<String, Object> payload = objectMapper.convertValue(
                    envelope.getPayload(), Map.class);
            String supplierId = (String) payload.get("supplierId");
            if (supplierId == null) return;

            Supplier supplier = entityStore.retrieve(supplierId);
            supplier.setTotalOrders(supplier.getTotalOrders() + 1);

            // Recalculate fulfillment rate
            int totalFulfilled = supplier.getTotalOrders() - supplier.getTotalReturns();
            if (supplier.getTotalOrders() > 0) {
                supplier.setFulfillmentRate(
                        (totalFulfilled * 100.0) / supplier.getTotalOrders());
            }

            entityStore.store(supplier);

            log.info("Supplier {}: updated metrics after order completion. TotalOrders={}, FulfillmentRate={}%",
                    supplierId, supplier.getTotalOrders(), supplier.getFulfillmentRate());

        } catch (Exception e) {
            log.error("Error processing order event for supplier metrics: {}", e.getMessage(), e);
        }
    }

    // -- review.events ------------------------------------------------------

    /**
     * Handles REVIEW_PUBLISHED events.
     * Updates the supplier's average rating.
     */
    @Transactional
    public void handleReviewEvent(EventEnvelope envelope) {
        if (envelope == null || envelope.getEventType() == null) {
            return;
        }

        if (!"REVIEW_PUBLISHED".equals(envelope.getEventType())) {
            return;
        }

        try {
            Map<String, Object> payload = objectMapper.convertValue(
                    envelope.getPayload(), Map.class);
            String supplierId = (String) payload.get("supplierId");
            Number newRating = (Number) payload.get("rating");
            if (supplierId == null || newRating == null) return;

            Supplier supplier = entityStore.retrieve(supplierId);

            // Running average: new_avg = (old_avg * count + new_rating) / (count + 1)
            // Using totalOrders as proxy for review count
            int reviewCount = Math.max(supplier.getTotalOrders(), 1);
            double currentAvg = supplier.getRating();
            double updatedAvg = ((currentAvg * reviewCount) + newRating.doubleValue()) / (reviewCount + 1);
            supplier.setRating(Math.round(updatedAvg * 100.0) / 100.0);

            entityStore.store(supplier);

            log.info("Supplier {}: updated rating to {} after review", supplierId, supplier.getRating());

        } catch (Exception e) {
            log.error("Error processing review event for supplier metrics: {}", e.getMessage(), e);
        }
    }

    // -- return.events ------------------------------------------------------

    /**
     * Handles RETURN_COMPLETED events.
     * Increments totalReturns and recalculates fulfillment rate.
     */
    @Transactional
    public void handleReturnEvent(EventEnvelope envelope) {
        if (envelope == null || envelope.getEventType() == null) {
            return;
        }

        if (!"RETURN_COMPLETED".equals(envelope.getEventType())) {
            return;
        }

        try {
            Map<String, Object> payload = objectMapper.convertValue(
                    envelope.getPayload(), Map.class);
            String supplierId = (String) payload.get("supplierId");
            if (supplierId == null) return;

            Supplier supplier = entityStore.retrieve(supplierId);
            supplier.setTotalReturns(supplier.getTotalReturns() + 1);

            // Recalculate fulfillment rate
            int totalFulfilled = supplier.getTotalOrders() - supplier.getTotalReturns();
            if (supplier.getTotalOrders() > 0) {
                supplier.setFulfillmentRate(
                        (totalFulfilled * 100.0) / supplier.getTotalOrders());
            }

            entityStore.store(supplier);

            log.info("Supplier {}: updated metrics after return. TotalReturns={}, FulfillmentRate={}%",
                    supplierId, supplier.getTotalReturns(), supplier.getFulfillmentRate());

        } catch (Exception e) {
            log.error("Error processing return event for supplier metrics: {}", e.getMessage(), e);
        }
    }
}
