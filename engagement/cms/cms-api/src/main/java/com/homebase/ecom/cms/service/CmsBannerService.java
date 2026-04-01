package com.homebase.ecom.cms.service;

import com.homebase.ecom.cms.dto.request.CreateBannerRequest;
import com.homebase.ecom.cms.dto.response.BannerResult;

public interface CmsBannerService {
    BannerResult create(CreateBannerRequest banner);
    BannerResult retrieve(String id);
    void delete(String id);
}
