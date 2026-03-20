package com.homebase.ecom.compliance.infrastructure.persistence.adapter;

import com.homebase.ecom.compliance.model.Regulation;
import com.homebase.ecom.compliance.port.out.RegulationRepository;
import com.homebase.ecom.compliance.infrastructure.persistence.entity.RegulationEntity;
import com.homebase.ecom.compliance.infrastructure.persistence.repository.RegulationJpaRepository;

import java.util.List;
import java.util.Optional;

public class RegulationRepositoryImpl implements RegulationRepository {

    private final RegulationJpaRepository jpaRepo;

    public RegulationRepositoryImpl(RegulationJpaRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Optional<Regulation> findById(String id) {
        return jpaRepo.findById(id).map(this::toModel);
    }

    @Override
    public List<Regulation> findByJurisdiction(String jurisdiction) {
        return jpaRepo.findByJurisdiction(jurisdiction).stream().map(this::toModel).toList();
    }

    @Override
    public List<Regulation> findActive() {
        return jpaRepo.findByActiveTrue().stream().map(this::toModel).toList();
    }

    @Override
    public Regulation save(Regulation regulation) {
        return toModel(jpaRepo.save(toEntity(regulation)));
    }

    private Regulation toModel(RegulationEntity entity) {
        Regulation model = new Regulation();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setRegulationType(entity.getRegulationType());
        model.setJurisdiction(entity.getJurisdiction());
        model.setDescription(entity.getDescription());
        model.setReferenceUrl(entity.getReferenceUrl());
        model.setActive(entity.isActive());
        return model;
    }

    private RegulationEntity toEntity(Regulation model) {
        RegulationEntity entity = new RegulationEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setRegulationType(model.getRegulationType());
        entity.setJurisdiction(model.getJurisdiction());
        entity.setDescription(model.getDescription());
        entity.setReferenceUrl(model.getReferenceUrl());
        entity.setActive(model.isActive());
        return entity;
    }
}
