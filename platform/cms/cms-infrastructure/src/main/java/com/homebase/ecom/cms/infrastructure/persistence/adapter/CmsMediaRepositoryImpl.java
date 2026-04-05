package com.homebase.ecom.cms.infrastructure.persistence.adapter;

import com.homebase.ecom.cms.model.CmsMedia;
import com.homebase.ecom.cms.model.port.CmsMediaRepository;
import com.homebase.ecom.cms.infrastructure.persistence.entity.CmsMediaEntity;
import com.homebase.ecom.cms.infrastructure.persistence.mapper.CmsMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CmsMediaRepositoryImpl implements CmsMediaRepository {

    private final CmsMediaJpaRepository jpaRepository;
    private final CmsMapper mapper;

    public CmsMediaRepositoryImpl(CmsMediaJpaRepository jpaRepository, CmsMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<CmsMedia> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public List<CmsMedia> findByFolder(String folder) {
        return jpaRepository.findByFolder(folder).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public CmsMedia save(CmsMedia media) {
        CmsMediaEntity entity = mapper.toEntity(media);
        CmsMediaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toModel(savedEntity);
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }
}
