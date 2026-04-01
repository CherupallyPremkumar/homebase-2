package com.homebase.ecom.auth.spi;

import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory for the Kafka Event Listener SPI.
 * Configured in Keycloak realm settings → Events → Event Listeners → "homebase-kafka".
 */
public class KafkaEventListenerProviderFactory implements EventListenerProviderFactory {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventListenerProviderFactory.class);
    private static final String PROVIDER_ID = "homebase-kafka";

    private KafkaEventPublisher kafkaPublisher;

    @Override
    public EventListenerProvider create(KeycloakSession session) {
        return new KafkaEventListenerProvider(session, kafkaPublisher);
    }

    @Override
    public void init(Config.Scope config) {
        String bootstrapServers = config.get("bootstrapServers", "localhost:9092");
        String topic = config.get("topic", "auth.user.events");
        kafkaPublisher = new KafkaEventPublisher(bootstrapServers, topic);
        log.info("HomeBase Kafka Event Listener initialized: servers={}, topic={}", bootstrapServers, topic);
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        // no-op
    }

    @Override
    public void close() {
        if (kafkaPublisher != null) {
            kafkaPublisher.close();
        }
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }
}
