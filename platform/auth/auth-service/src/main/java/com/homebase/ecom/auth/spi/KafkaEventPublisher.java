package com.homebase.ecom.auth.spi;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Simple Kafka producer for publishing auth events from inside Keycloak.
 * Lightweight — no Spring, no framework. Just plain Kafka client.
 */
public class KafkaEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventPublisher.class);

    private final KafkaProducer<String, String> producer;
    private final String topic;

    public KafkaEventPublisher(String bootstrapServers, String topic) {
        this.topic = topic;
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, "1");
        this.producer = new KafkaProducer<>(props);
    }

    public void publishUserRegistered(String userId, String email, String firstName, String lastName, String realm) {
        String json = String.format(
            "{\"eventType\":\"USER_REGISTERED\",\"keycloakUserId\":\"%s\",\"email\":\"%s\",\"firstName\":\"%s\",\"lastName\":\"%s\",\"realm\":\"%s\",\"timestamp\":%d}",
            userId, email, firstName != null ? firstName : "", lastName != null ? lastName : "", realm, System.currentTimeMillis()
        );
        send(userId, json);
    }

    public void publishUserUpdated(String userId, String realm, String updateType) {
        String json = String.format(
            "{\"eventType\":\"USER_UPDATED\",\"keycloakUserId\":\"%s\",\"realm\":\"%s\",\"updateType\":\"%s\",\"timestamp\":%d}",
            userId, realm, updateType, System.currentTimeMillis()
        );
        send(userId, json);
    }

    private void send(String key, String value) {
        try {
            producer.send(new ProducerRecord<>(topic, key, value));
            log.debug("Published to {}: {}", topic, value);
        } catch (Exception e) {
            log.error("Failed to publish to Kafka topic {}: {}", topic, e.getMessage());
        }
    }

    public void close() {
        producer.close();
    }
}
