package com.homebase.ecom.cms.infrastructure.configuration;

import com.homebase.ecom.cms.infrastructure.persistence.mapper.CmsMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CmsInfrastructureConfiguration {

    @Bean
    public CmsMapper cmsMapper() {
        return new CmsMapper();
    }
}
