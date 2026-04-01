package com.homebase.ecom.cms.infrastructure.persistence.adapter;

import com.homebase.ecom.cms.model.CmsSeoMeta;
import com.homebase.ecom.cms.model.port.CmsSeoMetaRepository;
import com.homebase.ecom.cms.infrastructure.persistence.entity.CmsSeoMetaEntity;
import com.homebase.ecom.cms.infrastructure.persistence.mapper.CmsMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CmsSeoMetaRepositoryImpl implements CmsSeoMetaRepository {

    private final CmsSeoMetaJpaRepository jpaRepository;
    private final CmsMapper mapper;

    public CmsSeoMetaRepositoryImpl(CmsSeoMetaJpaRepository jpaRepository, CmsMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<CmsSeoMeta> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public List<CmsSeoMeta> findByPageId(String pageId) {
        Optional<CmsSeoMetaEntity> entity = jpaRepository.findByPageId(pageId);
        return entity.map(e -> Collections.singletonList(mapper.toModel(e)))
                .orElse(Collections.emptyList());
    }

    @Override
    public CmsSeoMeta save(CmsSeoMeta cmsSeoMeta) {
        CmsSeoMetaEntity entity = mapper.toEntity(cmsSeoMeta);
        CmsSeoMetaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toModel(savedEntity);
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }
}
