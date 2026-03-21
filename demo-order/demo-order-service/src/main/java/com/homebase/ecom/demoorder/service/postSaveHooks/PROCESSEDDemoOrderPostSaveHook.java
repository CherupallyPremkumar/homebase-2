package com.homebase.ecom.demoorder.service.postSaveHooks;

import com.homebase.ecom.demoorder.event.DemoOrderProcessedEvent;
import com.homebase.ecom.demoorder.model.DemoOrder;
import com.homebase.ecom.demoorder.port.DemoOrderEventPublisherPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Post-save hook for PROCESSED state.
 * After a DemoOrder enters PROCESSED, publishes a DemoOrderProcessedEvent
 * via the event publisher port.
 */
public class PROCESSEDDemoOrderPostSaveHook implements PostSaveHook<DemoOrder> {

    private static final Logger log = LoggerFactory.getLogger(PROCESSEDDemoOrderPostSaveHook.class);

    private final DemoOrderEventPublisherPort eventPublisher;

    public PROCESSEDDemoOrderPostSaveHook(DemoOrderEventPublisherPort eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, DemoOrder entity, TransientMap map) {
        log.info("DemoOrder {} entered PROCESSED state, publishing event", entity.getId());
        var event = new DemoOrderProcessedEvent(entity.getId(), LocalDateTime.now());
        eventPublisher.publish(event);
    }
}
