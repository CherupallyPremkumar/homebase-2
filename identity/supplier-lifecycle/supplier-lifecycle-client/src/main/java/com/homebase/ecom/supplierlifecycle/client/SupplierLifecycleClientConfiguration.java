package com.homebase.ecom.supplierlifecycle.client;

import com.homebase.ecom.supplierlifecycle.service.SupplierLifecycleService;
import org.chenile.proxy.builder.ProxyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SupplierLifecycleClientConfiguration {
    @Bean
    public SupplierLifecycleService supplierLifecycleServiceClient(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            SupplierLifecycleService.class,
            "_supplierLifecycleStateEntityService_",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            "http://localhost:8080"
        );
    }
}
