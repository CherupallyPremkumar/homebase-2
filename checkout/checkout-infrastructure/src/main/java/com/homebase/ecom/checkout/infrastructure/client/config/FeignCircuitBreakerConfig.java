package com.homebase.ecom.checkout.infrastructure.client.config;

import feign.Logger;
import feign.Request;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static feign.Logger.Level;

/**
 * Feign configuration with Resilience4j circuit breaker support.
 * Provides circuit breaker configuration for all Feign clients in checkout
 * service.
 */
@Configuration
public class FeignCircuitBreakerConfig {

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                // Number of calls below which no failure percentage is evaluated
                .minimumNumberOfCalls(5)
                // Failure rate threshold to trigger circuit breaker (50%)
                .failureRateThreshold(50)
                // Wait duration before trying to close the circuit again
                .waitDurationInOpenState(Duration.ofSeconds(30))
                // Sliding window size for recording calls
                .slidingWindowSize(10)
                // Permitted number of calls in half-open state
                .permittedNumberOfCallsInHalfOpenState(3)
                // Slow call duration threshold (2 seconds)
                .slowCallDurationThreshold(Duration.ofSeconds(2))
                // Slow call rate threshold
                .slowCallRateThreshold(80)
                .build();

        return CircuitBreakerRegistry.of(config);
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Level.BASIC;
    }

    @Bean
    public Request.Options feignOptions() {
        return new Request.Options(
                // Connection timeout in milliseconds
                5000,
                TimeUnit.MILLISECONDS,
                // Read timeout in milliseconds
                10000,
                TimeUnit.MILLISECONDS,
                // Follow redirects
                true);
    }
}
