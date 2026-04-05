package com.homebase.ecom.promo.infrastructure.configuration;

import com.homebase.ecom.promo.infrastructure.integration.KafkaPromoEventPublisher;
import com.homebase.ecom.promo.port.PromoEventPublisherPort;
import org.chenile.pubsub.ChenilePub;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Infrastructure layer configuration for promo module.
 * Wires adapters to domain ports.
 */
@Configuration
public class PromoInfrastructureConfiguration {

    @Bean
    PromoEventPublisherPort promoEventPublisherPort(ChenilePub chenilePub) {
        return new KafkaPromoEventPublisher(chenilePub);
    }
}
