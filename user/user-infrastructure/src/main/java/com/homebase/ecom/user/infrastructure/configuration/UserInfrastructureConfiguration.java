package com.homebase.ecom.user.infrastructure.configuration;

import com.homebase.ecom.notification.service.NotificationService;
import com.homebase.ecom.user.domain.port.CurrencyResolver;
import com.homebase.ecom.user.domain.port.IdentityProviderPort;
import com.homebase.ecom.user.domain.port.NotificationPort;
import com.homebase.ecom.user.domain.port.UserEventPublisher;
import com.homebase.ecom.user.infrastructure.adapter.CurrencyResolverAdapter;
import com.homebase.ecom.user.infrastructure.adapter.IdentityProviderAdapter;
import com.homebase.ecom.user.infrastructure.adapter.NotificationAdapter;
import com.homebase.ecom.user.infrastructure.event.UserEventPublisherImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Infrastructure layer: wires adapters to domain ports for the User bounded context.
 *
 * Each adapter is a pure translator between domain ports and external infrastructure.
 * No @Component/@Service -- all beans declared explicitly via @Bean.
 */
@Configuration
public class UserInfrastructureConfiguration {

    @Bean("userCurrencyResolver")
    CurrencyResolver userCurrencyResolver() {
        return new CurrencyResolverAdapter();
    }

    @Bean("userIdentityProviderPort")
    IdentityProviderPort userIdentityProviderPort(
            @Value("${chenile.security.keycloak.host:http://localhost:8180}") String keycloakHost,
            @Value("${chenile.security.keycloak.base.realm:homebase}") String realm,
            @Value("${chenile.security.client.id:homebase-backend}") String clientId,
            @Value("${chenile.security.client.secret:change-me}") String clientSecret) {
        return new IdentityProviderAdapter(keycloakHost, realm, clientId, clientSecret);
    }

    @Bean("userNotificationPort")
    NotificationPort userNotificationPort(NotificationService notificationServiceClient) {
        return new NotificationAdapter(notificationServiceClient);
    }

    @Bean("userEventPublisher")
    UserEventPublisher userEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new UserEventPublisherImpl(applicationEventPublisher);
    }
}
