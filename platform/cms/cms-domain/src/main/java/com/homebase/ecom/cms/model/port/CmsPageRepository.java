package com.homebase.ecom.cms.model.port;

import com.homebase.ecom.cms.model.CmsPage;

import java.util.List;
import java.util.Optional;

public interface CmsPageRepository {
    Optional<CmsPage> findById(String id);
    Optional<CmsPage> findBySlug(String slug);
    List<CmsPage> findAllPublished();
    CmsPage save(CmsPage page);
    void delete(String id);
}
