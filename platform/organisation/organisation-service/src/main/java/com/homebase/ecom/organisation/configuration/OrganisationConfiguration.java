package com.homebase.ecom.organisation.configuration;

import com.homebase.ecom.organisation.infrastructure.persistence.adapter.OrganisationJpaRepository;
import com.homebase.ecom.organisation.infrastructure.persistence.adapter.OrganisationRepositoryImpl;
import com.homebase.ecom.organisation.infrastructure.persistence.mapper.OrganisationMapper;
import com.homebase.ecom.organisation.model.port.OrganisationRepository;
import com.homebase.ecom.organisation.service.OrganisationService;
import com.homebase.ecom.organisation.service.impl.OrganisationServiceImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrganisationConfiguration {

    @Bean
    public OrganisationMapper organisationMapper() {
        return new OrganisationMapper();
    }

    @Bean
    public OrganisationRepository organisationRepository(OrganisationJpaRepository jpaRepository, OrganisationMapper mapper) {
        return new OrganisationRepositoryImpl(jpaRepository, mapper);
    }

    @Bean
    public OrganisationService organisationService(OrganisationRepository repository) {
        return new OrganisationServiceImpl(repository);
    }
}
