package com.homebase.ecom.cms.service;

import com.homebase.ecom.cms.dto.BannerDto;
import com.homebase.ecom.cms.dto.CmsPageDto;

import java.util.List;

public interface CmsService {
    CmsPageDto getPage(String slug);
    CmsPageDto savePage(CmsPageDto page);
    List<BannerDto> getBanners(String position);
}
