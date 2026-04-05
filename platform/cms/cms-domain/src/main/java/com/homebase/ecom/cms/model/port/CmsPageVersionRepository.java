package com.homebase.ecom.cms.model.port;

import com.homebase.ecom.cms.model.CmsPageVersion;

import java.util.List;
import java.util.Optional;

public interface CmsPageVersionRepository {
    Optional<CmsPageVersion> findById(String id);
    List<CmsPageVersion> findByPageId(String pageId);
    CmsPageVersion save(CmsPageVersion version);
    void delete(String id);
}
