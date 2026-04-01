package com.homebase.ecom.organisation.service;

import com.homebase.ecom.organisation.model.Organisation;

/**
 * Organisation service — CRUD (no STM, company profile doesn't have a lifecycle).
 */
public interface OrganisationService {
    Organisation create(Organisation organisation);
    Organisation retrieve(String id);
    Organisation retrieveByTenant(String tenantId);
    Organisation update(String id, Organisation organisation);
}
