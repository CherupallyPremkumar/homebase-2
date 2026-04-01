package com.homebase.ecom.shared.configuration.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Kafka Dead Letter Queue (DLQ) behavior.
 *
 * Failed messages are retried up to {@code maxRetries} times with exponential backoff,
 * then forwarded to a dead letter topic named {@code {original-topic}.DLT}.
 */
@ConfigurationProperties(prefix = "kafka.dlq")
public class KafkaDlqProperties {

    /** Maximum number of retry attempts before sending to the DLT. */
    private int maxRetries = 3;

    /** Initial backoff interval in milliseconds. */
    private long initialIntervalMs = 1000;

    /** Multiplier applied to the backoff interval after each retry. */
    private double multiplier = 2.0;

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public long getInitialIntervalMs() {
        return initialIntervalMs;
    }

    public void setInitialIntervalMs(long initialIntervalMs) {
        this.initialIntervalMs = initialIntervalMs;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }
}
