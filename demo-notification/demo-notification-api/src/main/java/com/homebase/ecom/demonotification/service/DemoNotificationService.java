package com.homebase.ecom.demonotification.service;

import java.util.Map;

/**
 * Service interface for DemoNotification.
 * Published in demo-notification-api for cross-BC consumption.
 * The actual implementation extends HmStateEntityServiceImpl for STM lifecycle.
 */
public interface DemoNotificationService {

    /**
     * Handles incoming events from "demo-order.events" topic.
     * Chenile routes here via @EventsSubscribedTo on the controller.
     */
    void onOrderEvent(Map<String, Object> eventPayload);
}
