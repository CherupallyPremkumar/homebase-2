package com.homebase.ecom.cms.media.configuration;

import com.homebase.ecom.cms.infrastructure.persistence.adapter.CmsMediaJpaRepository;
import com.homebase.ecom.cms.infrastructure.persistence.adapter.CmsMediaRepositoryImpl;
import com.homebase.ecom.cms.infrastructure.persistence.mapper.CmsMapper;
import com.homebase.ecom.cms.model.port.CmsMediaRepository;
import com.homebase.ecom.cms.service.CmsMediaService;
import com.homebase.ecom.cms.media.service.impl.CmsMediaServiceImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CmsMediaConfiguration {

    @Bean
    public CmsMediaRepository cmsMediaRepository(CmsMediaJpaRepository jpaRepository, CmsMapper mapper) {
        return new CmsMediaRepositoryImpl(jpaRepository, mapper);
    }

    @Bean
    public CmsMediaServiceImpl cmsMediaServiceImpl(CmsMediaRepository cmsMediaRepository) {
        return new CmsMediaServiceImpl(cmsMediaRepository);
    }
}
