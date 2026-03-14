package com.homebase.ecom.build.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TenantConfiguration {

    @Bean
    DefaultTenantInterceptor defaultTenantInterceptor() {
        return new DefaultTenantInterceptor();
    }
}
