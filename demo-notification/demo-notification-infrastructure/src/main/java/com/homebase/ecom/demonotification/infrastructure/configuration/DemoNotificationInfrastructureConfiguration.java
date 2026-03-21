package com.homebase.ecom.demonotification.infrastructure.configuration;

import com.homebase.ecom.demonotification.infrastructure.persistence.mapper.DemoNotificationMapper;
import com.homebase.ecom.demonotification.infrastructure.persistence.repository.DemoNotificationJpaRepository;
import com.homebase.ecom.demonotification.model.DemoNotification;
import org.chenile.jpautils.store.ChenileJpaEntityStore;
import org.chenile.utils.entity.service.EntityStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Infrastructure configuration for demo-notification.
 * Wires EntityStore, Mapper beans.
 */
@Configuration
public class DemoNotificationInfrastructureConfiguration {

    @Bean
    DemoNotificationMapper demoNotifMapper() {
        return new DemoNotificationMapper();
    }

    @Bean
    EntityStore<DemoNotification> demoNotifEntityStore(
            DemoNotificationJpaRepository repo, DemoNotificationMapper mapper) {
        return new ChenileJpaEntityStore<>(repo,
                entity -> mapper.toModel(entity),
                model -> mapper.toEntity(model));
    }
}
