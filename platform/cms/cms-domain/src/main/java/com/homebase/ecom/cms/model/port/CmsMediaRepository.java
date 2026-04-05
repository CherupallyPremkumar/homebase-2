package com.homebase.ecom.cms.model.port;

import com.homebase.ecom.cms.model.CmsMedia;

import java.util.List;
import java.util.Optional;

public interface CmsMediaRepository {
    Optional<CmsMedia> findById(String id);
    List<CmsMedia> findByFolder(String folder);
    CmsMedia save(CmsMedia media);
    void delete(String id);
}
