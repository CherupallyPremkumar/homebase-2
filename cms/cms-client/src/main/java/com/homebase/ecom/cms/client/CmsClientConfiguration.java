package com.homebase.ecom.cms.client;

import com.homebase.ecom.cms.service.CmsService;
import org.chenile.proxy.builder.ProxyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CmsClientConfiguration {
    @Bean
    public CmsService cmsServiceClient(ProxyBuilder proxyBuilder) {
        return proxyBuilder.buildProxy(
            CmsService.class,
            "_cmsStateEntityService_",
            null,
            ProxyBuilder.ProxyMode.COMPUTE_DYNAMICALLY,
            "http://localhost:8080"
        );
    }
}
