package com.homebase.ecom.supplier.client;

import com.homebase.ecom.supplier.service.SupplierService;
import org.chenile.proxy.builder.ProxyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SupplierClientConfiguration {
    @Bean
    public SupplierService supplierServiceClient(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            SupplierService.class,
            "_supplierStateEntityService_",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            "http://localhost:8080"
        );
    }
}
