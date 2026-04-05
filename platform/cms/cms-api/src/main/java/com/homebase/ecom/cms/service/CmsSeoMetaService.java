package com.homebase.ecom.cms.service;

import com.homebase.ecom.cms.dto.request.CreateSeoMetaRequest;
import com.homebase.ecom.cms.dto.response.SeoMetaResult;

public interface CmsSeoMetaService {
    SeoMetaResult create(CreateSeoMetaRequest seoMeta);
    SeoMetaResult retrieve(String id);
    void delete(String id);
}
