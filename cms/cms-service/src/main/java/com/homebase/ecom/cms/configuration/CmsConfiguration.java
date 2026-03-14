package com.homebase.ecom.cms.configuration;

import com.homebase.ecom.cms.infrastructure.persistence.adapter.BannerJpaRepository;
import com.homebase.ecom.cms.infrastructure.persistence.adapter.BannerRepositoryImpl;
import com.homebase.ecom.cms.infrastructure.persistence.adapter.CmsPageJpaRepository;
import com.homebase.ecom.cms.infrastructure.persistence.adapter.CmsPageRepositoryImpl;
import com.homebase.ecom.cms.infrastructure.persistence.mapper.CmsMapper;
import com.homebase.ecom.cms.model.port.BannerRepository;
import com.homebase.ecom.cms.model.port.CmsPageRepository;
import com.homebase.ecom.cms.service.CmsService;
import com.homebase.ecom.cms.service.impl.CmsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CmsConfiguration {

    @Bean
    public CmsMapper cmsMapper() {
        return new CmsMapper();
    }

    @Bean
    public CmsPageRepository cmsPageRepository(CmsPageJpaRepository jpaRepository, CmsMapper mapper) {
        return new CmsPageRepositoryImpl(jpaRepository, mapper);
    }

    @Bean
    public BannerRepository bannerRepository(BannerJpaRepository jpaRepository, CmsMapper mapper) {
        return new BannerRepositoryImpl(jpaRepository, mapper);
    }

    @Bean
    public CmsService cmsService(CmsPageRepository pageRepository, BannerRepository bannerRepository, CmsMapper mapper) {
        return new CmsServiceImpl(pageRepository, bannerRepository, mapper);
    }
}
