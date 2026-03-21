package com.homebase.ecom.demoorder.infrastructure.configuration;

import com.homebase.ecom.demoorder.infrastructure.persistence.mapper.DemoOrderMapper;
import com.homebase.ecom.demoorder.infrastructure.persistence.repository.DemoOrderJpaRepository;
import com.homebase.ecom.demoorder.model.DemoOrder;
import org.chenile.jpautils.store.ChenileJpaEntityStore;
import org.chenile.utils.entity.service.EntityStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Infrastructure configuration for demo-order.
 * Wires EntityStore, Mapper beans.
 */
@Configuration
public class DemoOrderInfrastructureConfiguration {

    @Bean
    DemoOrderMapper demoOrderMapper() {
        return new DemoOrderMapper();
    }

    @Bean
    EntityStore<DemoOrder> demoOrderEntityStore(
            DemoOrderJpaRepository repo, DemoOrderMapper mapper) {
        return new ChenileJpaEntityStore<>(repo,
                entity -> mapper.toModel(entity),
                model -> mapper.toEntity(model));
    }
}
