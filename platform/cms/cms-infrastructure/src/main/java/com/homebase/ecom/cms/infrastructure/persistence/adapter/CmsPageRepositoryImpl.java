package com.homebase.ecom.cms.infrastructure.persistence.adapter;

import com.homebase.ecom.cms.model.CmsPage;
import com.homebase.ecom.cms.model.port.CmsPageRepository;
import com.homebase.ecom.cms.infrastructure.persistence.entity.CmsPageEntity;
import com.homebase.ecom.cms.infrastructure.persistence.mapper.CmsMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CmsPageRepositoryImpl implements CmsPageRepository {

    private final CmsPageJpaRepository jpaRepository;
    private final CmsMapper mapper;

    public CmsPageRepositoryImpl(CmsPageJpaRepository jpaRepository, CmsMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<CmsPage> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public Optional<CmsPage> findBySlug(String slug) {
        return jpaRepository.findBySlug(slug).map(mapper::toModel);
    }

    @Override
    public List<CmsPage> findAllPublished() {
        return jpaRepository.findAllByPublishedTrue().stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public CmsPage save(CmsPage page) {
        CmsPageEntity entity = mapper.toEntity(page);
        CmsPageEntity savedEntity = jpaRepository.save(entity);
        return mapper.toModel(savedEntity);
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }
}
