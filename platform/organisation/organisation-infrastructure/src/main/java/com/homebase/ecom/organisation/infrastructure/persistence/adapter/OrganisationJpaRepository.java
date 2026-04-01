package com.homebase.ecom.organisation.infrastructure.persistence.adapter;

import com.homebase.ecom.organisation.infrastructure.persistence.entity.OrganisationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganisationJpaRepository extends JpaRepository<OrganisationEntity, String> {
    Optional<OrganisationEntity> findByTenant(String tenant);
}
