package com.homebase.ecom.auth.spi;

import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Keycloak SPI Event Listener — deployed inside Keycloak.
 * Publishes user events to Kafka when users register, update, or get disabled.
 */
public class KafkaEventListenerProvider implements EventListenerProvider {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventListenerProvider.class);

    private final KeycloakSession session;
    private final KafkaEventPublisher kafkaPublisher;

    public KafkaEventListenerProvider(KeycloakSession session, KafkaEventPublisher kafkaPublisher) {
        this.session = session;
        this.kafkaPublisher = kafkaPublisher;
    }

    @Override
    public void onEvent(Event event) {
        if (event.getType() == EventType.REGISTER) {
            UserModel user = session.users().getUserById(session.getContext().getRealm(), event.getUserId());
            if (user != null) {
                kafkaPublisher.publishUserRegistered(
                        event.getUserId(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        event.getRealmName()
                );
                log.info("Published USER_REGISTERED event for {} ({})", user.getEmail(), event.getUserId());
            }
        } else if (event.getType() == EventType.UPDATE_PROFILE) {
            kafkaPublisher.publishUserUpdated(event.getUserId(), event.getRealmName(), "PROFILE_UPDATE");
            log.info("Published USER_UPDATED (PROFILE_UPDATE) for {}", event.getUserId());
        } else if (event.getType() == EventType.UPDATE_PASSWORD) {
            kafkaPublisher.publishUserUpdated(event.getUserId(), event.getRealmName(), "PASSWORD_CHANGE");
        }
    }

    @Override
    public void onEvent(AdminEvent event, boolean includeRepresentation) {
        // Admin events (role assignment, user disable) can be handled here
        log.debug("Admin event: {} on {}", event.getOperationType(), event.getResourcePath());
    }

    @Override
    public void close() {
        // no-op
    }
}
