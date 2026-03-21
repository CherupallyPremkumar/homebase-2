package com.homebase.ecom.demoorder.invm;

import com.homebase.ecom.demoorder.port.DemoOrderEventPublisherPort;
import org.chenile.core.event.EventProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

/**
 * Provides InVM demo-order event publisher as default.
 * If demo-order-kafka is on classpath, its bean takes precedence (via @Primary).
 */
@Configuration
public class DemoOrderInVMConfiguration {

    @Bean
    @ConditionalOnMissingBean(DemoOrderEventPublisherPort.class)
    DemoOrderEventPublisherPort demoOrderEventPublisherPort(
            EventProcessor eventProcessor, ObjectMapper objectMapper) {
        return new InVMDemoOrderEventPublisher(eventProcessor, objectMapper);
    }
}
