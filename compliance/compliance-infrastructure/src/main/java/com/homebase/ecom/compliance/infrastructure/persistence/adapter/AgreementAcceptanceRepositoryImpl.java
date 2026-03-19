package com.homebase.ecom.compliance.infrastructure.persistence.adapter;

import com.homebase.ecom.compliance.model.AgreementAcceptance;
import com.homebase.ecom.compliance.port.out.AgreementAcceptanceRepository;
import com.homebase.ecom.compliance.infrastructure.persistence.entity.AgreementAcceptanceEntity;
import com.homebase.ecom.compliance.infrastructure.persistence.repository.AgreementAcceptanceJpaRepository;

import java.util.List;
import java.util.Optional;

public class AgreementAcceptanceRepositoryImpl implements AgreementAcceptanceRepository {

    private final AgreementAcceptanceJpaRepository jpaRepo;

    public AgreementAcceptanceRepositoryImpl(AgreementAcceptanceJpaRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public AgreementAcceptance save(AgreementAcceptance acceptance) {
        AgreementAcceptanceEntity entity = toEntity(acceptance);
        AgreementAcceptanceEntity saved = jpaRepo.save(entity);
        return toModel(saved);
    }

    @Override
    public Optional<AgreementAcceptance> findByUserIdAndAgreementId(String userId, String agreementId) {
        return jpaRepo.findByUserIdAndAgreementId(userId, agreementId).map(this::toModel);
    }

    @Override
    public List<AgreementAcceptance> findByAgreementId(String agreementId) {
        return jpaRepo.findByAgreementId(agreementId).stream().map(this::toModel).toList();
    }

    @Override
    public List<AgreementAcceptance> findByUserId(String userId) {
        return jpaRepo.findByUserId(userId).stream().map(this::toModel).toList();
    }

    private AgreementAcceptance toModel(AgreementAcceptanceEntity entity) {
        AgreementAcceptance model = new AgreementAcceptance();
        model.setId(entity.getId());
        model.setAgreementId(entity.getAgreementId());
        model.setUserId(entity.getUserId());
        model.setUserType(entity.getUserType());
        model.setAcceptedAt(entity.getAcceptedAt());
        model.setIpAddress(entity.getIpAddress());
        model.setUserAgent(entity.getUserAgent());
        model.setTenant(entity.tenant);
        return model;
    }

    private AgreementAcceptanceEntity toEntity(AgreementAcceptance model) {
        AgreementAcceptanceEntity entity = new AgreementAcceptanceEntity();
        entity.setId(model.getId());
        entity.setAgreementId(model.getAgreementId());
        entity.setUserId(model.getUserId());
        entity.setUserType(model.getUserType());
        entity.setAcceptedAt(model.getAcceptedAt());
        entity.setIpAddress(model.getIpAddress());
        entity.setUserAgent(model.getUserAgent());
        entity.tenant = model.getTenant();
        return entity;
    }
}
