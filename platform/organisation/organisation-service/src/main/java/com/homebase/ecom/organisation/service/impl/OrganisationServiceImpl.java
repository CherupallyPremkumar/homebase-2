package com.homebase.ecom.organisation.service.impl;

import com.homebase.ecom.organisation.model.Organisation;
import com.homebase.ecom.organisation.model.port.OrganisationRepository;
import com.homebase.ecom.organisation.service.OrganisationService;
import org.chenile.base.exception.NotFoundException;

/**
 * Simple CRUD service — no STM workflow.
 */
public class OrganisationServiceImpl implements OrganisationService {

    private final OrganisationRepository repository;

    public OrganisationServiceImpl(OrganisationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Organisation create(Organisation organisation) {
        return repository.save(organisation);
    }

    @Override
    public Organisation retrieve(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(404, new Object[]{id}));
    }

    @Override
    public Organisation retrieveByTenant(String tenantId) {
        return repository.findByTenantId(tenantId)
                .orElseThrow(() -> new NotFoundException(404, new Object[]{tenantId}));
    }

    @Override
    public Organisation update(String id, Organisation organisation) {
        Organisation existing = retrieve(id);
        organisation.setId(existing.getId());
        return repository.save(organisation);
    }
}
