package com.homebase.ecom.cms.infrastructure.persistence.adapter;

import com.homebase.ecom.cms.model.CmsPageVersion;
import com.homebase.ecom.cms.model.port.CmsPageVersionRepository;
import com.homebase.ecom.cms.infrastructure.persistence.entity.CmsPageVersionEntity;
import com.homebase.ecom.cms.infrastructure.persistence.mapper.CmsMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CmsPageVersionRepositoryImpl implements CmsPageVersionRepository {

    private final CmsPageVersionJpaRepository jpaRepository;
    private final CmsMapper mapper;

    public CmsPageVersionRepositoryImpl(CmsPageVersionJpaRepository jpaRepository, CmsMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<CmsPageVersion> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public List<CmsPageVersion> findByPageId(String pageId) {
        return jpaRepository.findByPageIdOrderByVersionNumberDesc(pageId).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public CmsPageVersion save(CmsPageVersion version) {
        CmsPageVersionEntity entity = mapper.toEntity(version);
        CmsPageVersionEntity savedEntity = jpaRepository.save(entity);
        return mapper.toModel(savedEntity);
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }
}
