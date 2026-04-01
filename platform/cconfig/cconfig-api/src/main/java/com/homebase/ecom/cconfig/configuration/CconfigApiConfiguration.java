package com.homebase.ecom.cconfig.configuration;

import com.homebase.ecom.cconfig.sdk.cache.MemoryCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CconfigApiConfiguration {
    @Value("${chenile.config.path:com/homebase/ecom/config}")
    private String configPath;
    @Bean
    public MemoryCache memoryCache(){
        return new MemoryCache();
    }
}
