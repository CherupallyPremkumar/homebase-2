package com.homebase.ecom.shared.configuration.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.ExponentialBackOff;

/**
 * Kafka consumer error handling configuration with Dead Letter Queue support.
 *
 * When a consumer fails to process a message after the configured number of retries
 * (with exponential backoff), the failed record is published to a dead letter topic
 * named {@code {original-topic}.DLT}.
 *
 * This configuration is auto-activated when spring-kafka is on the classpath.
 * It customizes the default {@link CommonErrorHandler} used by all
 * {@code @KafkaListener} container factories.
 */
@Configuration
@ConditionalOnClass(KafkaOperations.class)
@EnableConfigurationProperties(KafkaDlqProperties.class)
public class KafkaErrorHandlerConfig {

    private static final Logger log = LoggerFactory.getLogger(KafkaErrorHandlerConfig.class);

    /**
     * Publishes failed records to the dead letter topic after retries are exhausted.
     * The DLT topic name follows Spring Kafka convention: {@code {original-topic}.DLT}.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    public DeadLetterPublishingRecoverer deadLetterPublishingRecoverer(
            KafkaOperations kafkaOperations) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaOperations);
        return recoverer;
    }

    /**
     * Configures the default error handler for all Kafka listener containers.
     * Uses exponential backoff retries before forwarding to the DLT.
     */
    @Bean
    public DefaultErrorHandler kafkaErrorHandler(
            DeadLetterPublishingRecoverer deadLetterPublishingRecoverer,
            KafkaDlqProperties dlqProperties) {

        ExponentialBackOff backOff = new ExponentialBackOff(
                dlqProperties.getInitialIntervalMs(),
                dlqProperties.getMultiplier());
        // maxRetries = maxElapsedTime is not straightforward with ExponentialBackOff,
        // so we set maxAttempts via the error handler directly
        backOff.setMaxElapsedTime(calculateMaxElapsedTime(dlqProperties));

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(
                deadLetterPublishingRecoverer, backOff);

        // Log each retry attempt
        errorHandler.setRetryListeners((ConsumerRecord<?, ?> record, Exception ex, int deliveryAttempt) -> {
            log.warn("Kafka retry attempt {}/{} for topic={}, partition={}, offset={}: {}",
                    deliveryAttempt, dlqProperties.getMaxRetries(),
                    record.topic(), record.partition(), record.offset(),
                    ex.getMessage());
        });

        log.info("Kafka DLQ error handler configured: maxRetries={}, initialInterval={}ms, multiplier={}",
                dlqProperties.getMaxRetries(), dlqProperties.getInitialIntervalMs(),
                dlqProperties.getMultiplier());

        return errorHandler;
    }

    /**
     * Calculate a generous max elapsed time that allows all configured retries to complete.
     * With exponential backoff: total = initial * (multiplier^retries - 1) / (multiplier - 1)
     * We add a safety margin to avoid cutting off the last retry.
     */
    private long calculateMaxElapsedTime(KafkaDlqProperties props) {
        double total = 0;
        double interval = props.getInitialIntervalMs();
        for (int i = 0; i < props.getMaxRetries(); i++) {
            total += interval;
            interval *= props.getMultiplier();
        }
        // Add 50% margin to ensure all retries complete
        return (long) (total * 1.5);
    }
}
