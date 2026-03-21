package com.homebase.ecom.offer.infrastructure.configuration;

import com.homebase.ecom.offer.domain.port.NotificationPort;
import com.homebase.ecom.offer.domain.port.OfferEventPublisherPort;
import com.homebase.ecom.offer.domain.port.PricingPort;
import com.homebase.ecom.offer.infrastructure.adapter.KafkaOfferEventPublisher;
import com.homebase.ecom.offer.infrastructure.adapter.NotificationAdapter;
import com.homebase.ecom.offer.infrastructure.adapter.PricingAdapter;
import org.chenile.pubsub.ChenilePub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

/**
 * Infrastructure layer: wires adapters to domain ports for the Offer bounded context.
 *
 * Each adapter is a pure translator between domain ports and external infrastructure.
 * No @Component/@Service -- all beans declared explicitly via @Bean.
 */
@Configuration
public class OfferInfrastructureConfiguration {

    @Bean("offerPricingPort")
    PricingPort offerPricingPort() {
        return new PricingAdapter();
    }

    @Bean("offerNotificationPort")
    NotificationPort offerNotificationPort() {
        return new NotificationAdapter();
    }

    @Bean("offerEventPublisherPort")
    @ConditionalOnBean(ChenilePub.class)
    OfferEventPublisherPort offerEventPublisherPort(
            ChenilePub chenilePub,
            ObjectMapper objectMapper) {
        return new KafkaOfferEventPublisher(chenilePub, objectMapper);
    }
}
