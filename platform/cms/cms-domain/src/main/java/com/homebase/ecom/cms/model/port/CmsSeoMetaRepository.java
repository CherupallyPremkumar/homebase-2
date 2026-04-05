package com.homebase.ecom.cms.model.port;

import com.homebase.ecom.cms.model.CmsSeoMeta;

import java.util.List;
import java.util.Optional;

public interface CmsSeoMetaRepository {
    Optional<CmsSeoMeta> findById(String id);
    List<CmsSeoMeta> findByPageId(String pageId);
    CmsSeoMeta save(CmsSeoMeta cmsSeoMeta);
    void delete(String id);
}
