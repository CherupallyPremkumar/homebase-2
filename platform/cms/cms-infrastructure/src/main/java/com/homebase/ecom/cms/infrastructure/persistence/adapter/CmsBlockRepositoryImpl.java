package com.homebase.ecom.cms.infrastructure.persistence.adapter;

import com.homebase.ecom.cms.model.CmsBlock;
import com.homebase.ecom.cms.model.port.CmsBlockRepository;
import com.homebase.ecom.cms.infrastructure.persistence.entity.CmsBlockEntity;
import com.homebase.ecom.cms.infrastructure.persistence.mapper.CmsMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CmsBlockRepositoryImpl implements CmsBlockRepository {

    private final CmsBlockJpaRepository jpaRepository;
    private final CmsMapper mapper;

    public CmsBlockRepositoryImpl(CmsBlockJpaRepository jpaRepository, CmsMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<CmsBlock> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public List<CmsBlock> findByPageId(String pageId) {
        return jpaRepository.findByPageIdOrderBySortOrderAsc(pageId).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public CmsBlock save(CmsBlock block) {
        CmsBlockEntity entity = mapper.toEntity(block);
        CmsBlockEntity savedEntity = jpaRepository.save(entity);
        return mapper.toModel(savedEntity);
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }
}
