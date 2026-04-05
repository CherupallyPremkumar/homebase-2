package com.homebase.ecom.cms.model.port;

import com.homebase.ecom.cms.model.CmsBlock;

import java.util.List;
import java.util.Optional;

public interface CmsBlockRepository {
    Optional<CmsBlock> findById(String id);
    List<CmsBlock> findByPageId(String pageId);
    CmsBlock save(CmsBlock block);
    void delete(String id);
}
