package com.homebase.ecom.supplierlifecycle.consumer;

import com.homebase.ecom.supplierlifecycle.domain.model.SupplierLifecycleSaga;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Kafka consumer that listens on the SUPPLIER_EVENTS topic for supplier
 * state change events. When a supplier transitions to SUSPENDED or BLACKLISTED,
 * it creates a saga with the suspend-flow. When REACTIVATED, it creates a
 * saga with the reactivate-flow. The STM then drives the saga through its steps.
 */
@Component
public class SupplierStateChangeEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(SupplierStateChangeEventConsumer.class);

    public static final String SUPPLIER_EVENTS_TOPIC = "supplier.events";

    private final StateEntityServiceImpl<SupplierLifecycleSaga> stateEntityService;

    public SupplierStateChangeEventConsumer(
            @Qualifier("_supplierLifecycleStateEntityService_") StateEntityServiceImpl<SupplierLifecycleSaga> stateEntityService) {
        this.stateEntityService = stateEntityService;
    }

    @KafkaListener(topics = SUPPLIER_EVENTS_TOPIC, groupId = "supplier-lifecycle-group")
    public void onSupplierStateChange(Map<String, Object> event) {
        String supplierId = (String) event.get("supplierId");
        String newState = (String) event.get("newState");
        String reason = (String) event.get("reason");

        if (supplierId == null || newState == null) {
            log.warn("Received supplier event with missing supplierId or newState, skipping: {}", event);
            return;
        }

        log.info("Received supplier state change event: supplierId={}, newState={}", supplierId, newState);

        try {
            String action;
            String flowId;

            switch (newState.toUpperCase()) {
                case "SUSPENDED":
                    action = "SUSPEND";
                    flowId = "suspend-flow";
                    break;
                case "BLACKLISTED":
                    action = "BLACKLIST";
                    flowId = "suspend-flow";
                    break;
                case "REACTIVATED":
                case "ACTIVE":
                    action = "REACTIVATE";
                    flowId = "reactivate-flow";
                    break;
                default:
                    log.debug("Ignoring supplier state change to: {} for supplier: {}", newState, supplierId);
                    return;
            }

            SupplierLifecycleSaga saga = new SupplierLifecycleSaga();
            saga.setSupplierId(supplierId);
            saga.setAction(action);
            saga.setReason(reason != null ? reason : "State change event: " + newState);
            saga.setFlowId(flowId);

            stateEntityService.create(saga);

            log.info("{} saga created for supplier: {}", action, supplierId);

        } catch (RuntimeException e) {
            log.warn("Idempotency: supplier lifecycle saga for supplier {} / state {} already created (possible replay). Skipping. Detail: {}",
                    supplierId, newState, e.getMessage());
        } catch (Exception e) {
            log.error("Error processing supplier state change for supplier: {}, newState: {}",
                    supplierId, newState, e);
        }
    }
}
