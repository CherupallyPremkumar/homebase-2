package com.homebase.ecom.cms.infrastructure.persistence;

import com.homebase.ecom.cms.model.CmsPage;
import com.homebase.ecom.cms.model.port.CmsConfigPort;
import com.homebase.ecom.cms.infrastructure.persistence.adapter.CmsPageJpaRepository;
import com.homebase.ecom.cms.infrastructure.persistence.entity.CmsPageEntity;
import com.homebase.ecom.cms.infrastructure.persistence.mapper.CmsMapper;
import com.homebase.ecom.core.jpa.ChenileJpaEntityStore;
import org.chenile.base.exception.BadRequestException;

/**
 * Bridges Chenile STM's EntityStore with JPA persistence for CmsPage.
 * Validates against cconfig policies before storing.
 */
public class ChenileCmsPageEntityStore extends ChenileJpaEntityStore<CmsPage, CmsPageEntity> {

    private final CmsConfigPort configPort;

    public ChenileCmsPageEntityStore(CmsPageJpaRepository repository, CmsMapper mapper, CmsConfigPort configPort) {
        super(repository,
                entity -> mapper.toModel(entity),
                model -> mapper.toEntity(model),
                (existing, updated) -> mapper.mergeEntity(existing, updated));
        this.configPort = configPort;
    }

    @Override
    public void store(CmsPage page) {
        // Validate before save
        if (page.getTitle() == null || page.getTitle().isBlank()) {
            throw new BadRequestException(30001, new Object[]{});
        }
        if (page.getTitle().length() > configPort.getPageTitleMaxLength()) {
            throw new BadRequestException(30002, new Object[]{configPort.getPageTitleMaxLength()});
        }
        if (page.getSlug() == null || page.getSlug().isBlank()) {
            throw new BadRequestException(30003, new Object[]{});
        }
        super.store(page);
    }
}
