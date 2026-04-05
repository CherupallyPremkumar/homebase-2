package com.homebase.ecom.user.service.postSaveHooks;

import com.homebase.ecom.user.domain.model.User;
import com.homebase.ecom.user.domain.port.UserEventPublisher;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Triggered when user enters KYC_VERIFIED state.
 * Publishes KYC verification event for downstream services.
 */
public class KYC_VERIFIEDUserPostSaveHook implements PostSaveHook<User> {

    private static final Logger log = LoggerFactory.getLogger(KYC_VERIFIEDUserPostSaveHook.class);

    private final UserEventPublisher eventPublisher;

    public KYC_VERIFIEDUserPostSaveHook(UserEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, User user, TransientMap map) {
        log.info("User {} KYC verified. Role: {}", user.getId(), user.getRole());
        // KYC verification event is published via the user.events topic through the
        // Kafka PostSaveHook mechanism in production.
    }
}
