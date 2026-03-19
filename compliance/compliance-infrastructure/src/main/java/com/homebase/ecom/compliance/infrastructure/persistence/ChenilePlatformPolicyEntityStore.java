package com.homebase.ecom.compliance.infrastructure.persistence;

import org.chenile.utils.entity.service.EntityStore;
import com.homebase.ecom.compliance.model.PlatformPolicy;
import com.homebase.ecom.compliance.infrastructure.persistence.entity.PlatformPolicyEntity;
import com.homebase.ecom.compliance.infrastructure.persistence.repository.PlatformPolicyJpaRepository;
import com.homebase.ecom.compliance.infrastructure.persistence.mapper.PlatformPolicyMapper;

public class ChenilePlatformPolicyEntityStore implements EntityStore<PlatformPolicy> {

    private final PlatformPolicyJpaRepository jpaRepository;
    private final PlatformPolicyMapper mapper;

    public ChenilePlatformPolicyEntityStore(PlatformPolicyJpaRepository jpaRepository, PlatformPolicyMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void store(PlatformPolicy entity) {
        PlatformPolicyEntity jpaEntity = mapper.toEntity(entity);
        PlatformPolicyEntity saved = jpaRepository.save(jpaEntity);
        // Copy back generated fields onto the original domain object
        entity.setId(saved.getId());
        entity.setVersion(saved.getVersion());
        entity.setCreatedTime(saved.getCreatedTime());
        entity.setLastModifiedTime(saved.getLastModifiedTime());
    }

    @Override
    public PlatformPolicy retrieve(String id) {
        return jpaRepository.findById(id)
                .map(mapper::toModel)
                .orElse(null);
    }
}
