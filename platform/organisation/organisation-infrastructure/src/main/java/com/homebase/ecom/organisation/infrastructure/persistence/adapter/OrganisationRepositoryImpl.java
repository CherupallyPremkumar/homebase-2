package com.homebase.ecom.organisation.infrastructure.persistence.adapter;

import com.homebase.ecom.organisation.model.Organisation;
import com.homebase.ecom.organisation.model.port.OrganisationRepository;
import com.homebase.ecom.organisation.infrastructure.persistence.mapper.OrganisationMapper;

import java.util.Optional;

public class OrganisationRepositoryImpl implements OrganisationRepository {

    private final OrganisationJpaRepository jpaRepository;
    private final OrganisationMapper mapper;

    public OrganisationRepositoryImpl(OrganisationJpaRepository jpaRepository, OrganisationMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Organisation save(Organisation organisation) {
        var entity = mapper.toEntity(organisation);
        var saved = jpaRepository.save(entity);
        return mapper.toModel(saved);
    }

    @Override
    public Optional<Organisation> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public Optional<Organisation> findByTenantId(String tenantId) {
        return jpaRepository.findByTenant(tenantId).map(mapper::toModel);
    }
}
