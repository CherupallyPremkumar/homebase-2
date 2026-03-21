package com.homebase.ecom.demonotification.service.impl;

import com.homebase.ecom.demonotification.model.DemoNotification;
import com.homebase.ecom.demonotification.service.DemoNotificationService;
import org.chenile.stm.STM;
import org.chenile.stm.impl.STMActionsInfoProvider;
import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.service.impl.HmStateEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * Implementation of DemoNotificationService.
 * Extends HmStateEntityServiceImpl to get full STM lifecycle support,
 * and adds onOrderEvent for cross-service event handling.
 *
 * This module has ZERO compile-time dependency on demo-order.
 * The two modules communicate only via the topic name "demo-order.events".
 */
public class DemoNotificationServiceImpl extends HmStateEntityServiceImpl<DemoNotification>
        implements DemoNotificationService {

    private static final Logger log = LoggerFactory.getLogger(DemoNotificationServiceImpl.class);

    private final ObjectMapper objectMapper;

    public DemoNotificationServiceImpl(STM<DemoNotification> stm,
                                        STMActionsInfoProvider actionsInfoProvider,
                                        EntityStore<DemoNotification> entityStore,
                                        ObjectMapper objectMapper) {
        super(stm, actionsInfoProvider, entityStore);
        this.objectMapper = objectMapper;
    }

    @Override
    public void onOrderEvent(Map<String, Object> eventPayload) {
        log.info("Received order event: {}", eventPayload);
        try {
            String eventType = (String) eventPayload.get("eventType");
            String orderId = (String) eventPayload.get("orderId");

            if ("ORDER_PROCESSED".equals(eventType)) {
                log.info("Processing ORDER_PROCESSED event for orderId={}", orderId);

                DemoNotification notification = new DemoNotification();
                notification.setOrderId(orderId);
                notification.setMessage("Order " + orderId + " has been processed");
                notification.setChannel("EMAIL");

                create(notification);
                log.info("Created notification for orderId={}", orderId);
            } else {
                log.debug("Ignoring event type: {}", eventType);
            }
        } catch (Exception e) {
            log.error("Failed to handle order event: {}", e.getMessage(), e);
        }
    }
}
