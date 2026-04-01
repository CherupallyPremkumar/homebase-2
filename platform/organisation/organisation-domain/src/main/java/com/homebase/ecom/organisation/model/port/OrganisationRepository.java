package com.homebase.ecom.organisation.model.port;

import com.homebase.ecom.organisation.model.Organisation;

import java.util.Optional;

/**
 * Domain port — driven port for Organisation persistence.
 */
public interface OrganisationRepository {
    Organisation save(Organisation organisation);
    Optional<Organisation> findById(String id);
    Optional<Organisation> findByTenantId(String tenantId);
}
