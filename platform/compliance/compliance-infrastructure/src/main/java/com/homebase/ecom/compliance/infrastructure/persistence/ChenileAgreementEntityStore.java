package com.homebase.ecom.compliance.infrastructure.persistence;

import org.chenile.utils.entity.service.EntityStore;
import com.homebase.ecom.compliance.model.Agreement;
import com.homebase.ecom.compliance.infrastructure.persistence.entity.AgreementEntity;
import com.homebase.ecom.compliance.infrastructure.persistence.repository.AgreementJpaRepository;
import com.homebase.ecom.compliance.infrastructure.persistence.mapper.AgreementMapper;

public class ChenileAgreementEntityStore implements EntityStore<Agreement> {

    private final AgreementJpaRepository jpaRepository;
    private final AgreementMapper mapper;

    public ChenileAgreementEntityStore(AgreementJpaRepository jpaRepository, AgreementMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void store(Agreement entity) {
        AgreementEntity jpaEntity = mapper.toEntity(entity);
        AgreementEntity saved = jpaRepository.save(jpaEntity);
        // Copy back generated fields onto the original domain object
        entity.setId(saved.getId());
        entity.setVersion(saved.getVersion());
        entity.setCreatedTime(saved.getCreatedTime());
        entity.setLastModifiedTime(saved.getLastModifiedTime());
    }

    @Override
    public Agreement retrieve(String id) {
        return jpaRepository.findById(id)
                .map(mapper::toModel)
                .orElse(null);
    }
}
