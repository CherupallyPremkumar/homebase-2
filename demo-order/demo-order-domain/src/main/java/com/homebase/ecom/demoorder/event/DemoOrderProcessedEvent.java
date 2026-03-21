package com.homebase.ecom.demoorder.event;

import java.time.LocalDateTime;

/**
 * Published when a demo order transitions to PROCESSED state.
 */
public class DemoOrderProcessedEvent extends DemoOrderEvent {

    public DemoOrderProcessedEvent() {}

    public DemoOrderProcessedEvent(String orderId, LocalDateTime timestamp) {
        super("ORDER_PROCESSED", orderId, timestamp);
    }
}
